package com.freightmate.harbour.controller;

import com.freightmate.harbour.helper.ListHelper;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.AddressDTO;
import com.freightmate.harbour.service.AddressService;
import com.freightmate.harbour.service.PostCodeService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private PostCodeService postCodeService;

    @Autowired
    private AddressService addressService;

    private static final Logger LOG = LoggerFactory.getLogger(AddressController.class);

    /**
     * Lookup using locality string or postcode and combine results
     * @param locality String to search for eg "keysb" or "3000"
     * @return list of matches, 204 when no results but lookup succeeded
     */
    @RequestMapping(path="/locality", method = RequestMethod.GET)
    public ResponseEntity<AuspostLocalityWrapper> fetchLocality(@Param("locality") String locality){

        // Make sure we have a valid request
        if(Strings.isBlank(locality)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        try {
            AuspostLocalityWrapper wrapper = postCodeService.getLocality(locality);

            // When we call auspost if we don't get any localities back lets send a 204
            if (Objects.isNull(wrapper.getLocalities()) || wrapper.getLocalities().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }

            return ResponseEntity.ok(wrapper);

        } catch (HttpClientErrorException e) {
            // If something unexpected happens. Send a 500
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    /**
     * Address book controller to get a list of addresses based on the User ID
     * The result depends on the pagination information provided when calling the endpoint
     *
     * @param addressType       optional enumerated string of DELIVERY and SENDER that identifies the resultset of the query
     *                          Omitting this argument will result the query to take both types
     * @param pageable          pagination for the query resultset. This is done by passing the page and size parameter in the URL
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return list of paginated addresses, return 204 if there is no result
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AddressQueryResult> readAddressBook(@RequestParam Optional<AddressType> addressType,
                                                              Pageable pageable,
                                                              Authentication authentication) {
        // Get the User ID of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        try {
            // perform read to address repository by calling the address book service
            List<AddressDTO> addresses = addressService
                    .readAddress(
                            authToken.getUserId(),
                            authToken.getRole(),
                            addressType.orElse(AddressType.ANY),
                            pageable
                    )
                    .stream()
                    .map(AddressDTO::fromAddress)
                    .collect(Collectors.toList());

            if (addresses.size() == 0) {
                // Return 204 if zero result
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }

            return ResponseEntity.ok(
                    AddressQueryResult
                            .builder()
                            .addresses(addresses)
                            .count(addresses.size())
                            .build()
            );

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Address book controller to create new address by validating the suburb information before insertion
     *
     * @param addressRequest    Expecting a JSON formatted body with the new address details
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return the new address after successful insertion. 400 code will be returned if unable to validate or insert
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AddressDTO> createAddress(@RequestBody Address addressRequest, Authentication authentication) {
        // Get the username of the requestor
        String username = ((AuthToken) authentication.getPrincipal()).getUsername();

        // validate postcode
        if(postCodeService.isInvalidPostcode(String.valueOf(addressRequest.getPostcode()))){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // validate suburb (town) name
        List<AuspostLocality> matchingLocals = postCodeService.getMatchingLocalitiesBySuburb(addressRequest);

        // if no matching suburb name return bad request
        if (matchingLocals.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // Set the Auspost suburb, postcode and state into the new address to ensure that we have consistent data
        addressRequest.setTown(matchingLocals.get(0).getLocation());
        addressRequest.setPostcode(matchingLocals.get(0).getPostcode());
        addressRequest.setState(matchingLocals.get(0).getState());

        try {
            // perform create address
            Address result = addressService.createAddress(addressRequest, username);

            if(Objects.isNull(result)) {
                // return 500 if address service is unable to create the record
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            return ResponseEntity.ok(AddressDTO.fromAddress(result));

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Address book controller to update existing address. Any non-null fields provided by the update request
     * will update the existing data. Null fields will be ignored.
     *
     * @param addressDto    Expecting a JSON formatted body with the updated address details
     * @return the updated address after successfully updated. 400 code will be returned if unable to validate or update
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateAddress(@RequestBody AddressDTO addressDto) {
        // validate if the address id exists
        //Address currentAddress = addressService.getAddressById(addressRequest.getId());
        Address currentAddress = addressService.getAddresses(
                Collections.singletonList(
                        addressDto.getId()
                )
        ).get(0);

        if(Objects.isNull(currentAddress)) {
        return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // validate postcode
        if(postCodeService.isInvalidPostcode(String.valueOf(addressDto.getPostcode()))){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // validate suburb (town) name
        List<AuspostLocality> matchingLocals = postCodeService.getMatchingLocalitiesBySuburb(AddressDTO.toAddress(addressDto));

        // if no matching suburb name return bad request
        if (matchingLocals.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // Set the Auspost suburb, postcode and state into the new address to ensure that we have consistent data
        addressDto.setTown(matchingLocals.get(0).getLocation());
        addressDto.setPostcode(matchingLocals.get(0).getPostcode());
        addressDto.setState(matchingLocals.get(0).getState());

        try {
            // perform update address
            Address result = addressService.updateAddress(addressDto, currentAddress);

            if(Objects.isNull(result)) {
                // return bad request if address service is unable to create the record
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Address book controller to soft delete existing address(es)
     *
     * @param ids               An array of address IDs that are expected to be deleted
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return a string of messages of addresses that have been deleted.
     */
    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAddress(@RequestParam("ids") List<Long> ids,
                                                Authentication authentication) {
        // Get the User ID of the requestor
        long userId = ((AuthToken) authentication.getPrincipal()).getUserId();

        try {
            // validate all the address Ids exist
            List<Address> addressesByIds = addressService.getAddresses(ids);
            if(ids.size() != addressesByIds.size()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // perform delete address for the user
            addressService.deleteAddresses(ids, userId);

            // return 204 if there is a successful delete
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @RequestMapping(path="/search", method = RequestMethod.POST)
    public ResponseEntity<List<AddressDTO>> searchAddress(@RequestParam Optional<AddressType> addressType,
                                                          @RequestParam String criteria,
                                                          Authentication authentication) {
        // Get the User ID of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();
        try {
            return ResponseEntity
                    .ok(
                        addressService.searchAddresses(
                                criteria,
                                authToken.getRole(),
                                authToken.getUserId(),
                                addressType.orElse(AddressType.ANY)
                        )
                        .stream()
                        .map(AddressDTO::fromAddress)
                        .collect(Collectors.toList())
                    );
        } catch (DataAccessException e) {
            LOG.error("Unable to access database: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }
}
