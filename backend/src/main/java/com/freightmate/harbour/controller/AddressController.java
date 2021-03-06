package com.freightmate.harbour.controller;

import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.AddressDTO;
import com.freightmate.harbour.model.dto.SuburbDTO;
import com.freightmate.harbour.service.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private PostCodeService postCodeService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService detailsService;

    @Autowired
    private SuburbService suburbService;

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
     * @param clientId          The targeted clientId. Will override the logged in user.
     * @param pageable          pagination for the query resultset. This is done by passing the page and size parameter in the URL
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return list of paginated addresses, return 204 if there is no result
     */
    @GetMapping
    public ResponseEntity<AddressQueryResult> readAddressBook(@RequestParam Optional<AddressType> addressType,
                                                              @RequestParam Optional<Long> clientId,
                                                              Pageable pageable,
                                                              Authentication authentication) {
        // Get the User ID of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // Check whether the Client ID belongs to the logged in user
        if(clientId.isPresent()) {
            if(!userService.isChildOf(authToken.getUserId(), clientId.get())) {
                throw new ForbiddenException("User does not have permission to read the address");
            }
        }

        try {
            // perform read to address repository by calling the address book service
            List<AddressDTO> addresses = addressService
                    .readAddress(
                            clientId.orElse(authToken.getUserId()),
                            (clientId.isEmpty() ? authToken.getRole() : UserRole.CLIENT),
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

        } catch (DataAccessException e) {
            LOG.error("Cannot read address: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Address book controller to create new address by validating the suburb information before insertion
     *
     * @param addressRequest    Expecting a JSON formatted body with the new address details
     * @param authentication    Spring security authentication object to get the current
     *                          session user info. Not needed
     *                          to pass in the API URL
     * @return the new address after successful insertion. 400 code will be returned if unable to validate or insert
     */
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody Address addressRequest,
                                                    Authentication authentication) {
        // Get the user ID of the requestor
        long userId = ((AuthToken) authentication.getPrincipal()).getUserId();

        // validate postcode
        if(suburbService.isInvalidPostcode(String.valueOf(addressRequest.getSuburb().getPostcode()))){
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
        // First we ensure that the suburb we pull from DB are having the value as per Auspost
        Suburb suburb = suburbService.populateSuburbFromAuspost(matchingLocals);
        // Second we set the suburb into the new address request object
        addressRequest.setSuburb(suburb);

        // Check whether the Client ID belongs to the logged in user
        if (Objects.nonNull(addressRequest.getUserClientId()) && !userService.isChildOf(userId, addressRequest.getUserClientId())) {
            throw new ForbiddenException("User does not have permission to update the address");
        }

        try {
            // perform create address
            Address result = addressService.createAddress(
                    addressRequest,
                    (Objects.nonNull(addressRequest.getUserClientId()) ? addressRequest.getUserClientId() : userId)
            );

            if(Objects.isNull(result)) {
                // return 500 if address service is unable to create the record
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            return ResponseEntity.ok(AddressDTO.fromAddress(result));

        } catch (DataAccessException e) {
            LOG.error("Cannot create address: ", e);
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
    @Transactional
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateAddress(@RequestBody AddressDTO addressDto, Authentication authentication) {
        // Get user details from the logged in user
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // validate if the address id exists
        //Address currentAddress = addressService.getAddressById(addressRequest.getId());
        Address currentAddress = addressService.getFirst(addressDto.getId());

        // validate postcode
        if(suburbService.isInvalidPostcode(
                String.valueOf(
                        addressDto
                                .getSuburb()
                                .getPostcode()
                )
        )){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        // Get the owner
        User owner = detailsService.getFirst(
                Objects.nonNull(addressDto.getUserClientId()) ?  addressDto.getUserClientId() : authToken.getUserId()
        );

        // validate suburb (town) name
        List<AuspostLocality> matchingLocals = postCodeService.getMatchingLocalitiesBySuburb(
                AddressDTO.toAddress(
                        addressDto,
                        owner
                )
        );

        // if no matching suburb name return bad request
        if (matchingLocals.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        try {
            // First we ensure that the suburb we pull from DB are having the value as per Auspost
            Suburb suburb = suburbService.populateSuburbFromAuspost(matchingLocals);
            // Second we set the suburb into the new address request object
            addressDto.setSuburb(SuburbDTO.fromSuburb(suburb));

            // perform update address
            Address result = addressService.updateAddress(
                    addressDto,
                    currentAddress,
                    authToken.getUserId()
            );

            if(Objects.isNull(result)) {
                // return bad request if address service is unable to create the record
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            LOG.error("Cannot update address: ", e);
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
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        try {
            // perform delete address for the user
            addressService.deleteAddresses(ids,
                    authToken.getUserId(),
                    authToken.getRole());

            // return 204 if there is a successful delete
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        } catch (DataAccessException e) {
            LOG.error("Cannot delete address: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Address book controller to search addresses with criterie
     *
     * @param addressType       This is an optional field. The value can either be DELIVERY, SENDER, or ANY
     * @param clientId          This is an optional field. When provided, it will perform a search to that user's
     *                          address list
     * @param criteria          This is the search criteria where it will be used by the controller to locate the matching
     *                          address details. The lookup is performed to a concatenation of multiple address fields
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return a list of addresses that matched to the search criteria
     */
    @RequestMapping(path="/search", method = RequestMethod.POST)
    public ResponseEntity<List<AddressDTO>> searchAddress(@RequestParam Optional<AddressType> addressType,
                                                          @RequestParam Optional<Long> clientId,
                                                          @RequestParam String criteria,
                                                          Authentication authentication) {
        // Get the User ID of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // Check if the client ID belongs to the logged in user
        if(clientId.isPresent()) {
            if (!userService.isChildOf(authToken.getUserId(), clientId.get())) {
                throw new ForbiddenException("User does not have permission to search address");
            }
        }
        try {
            return ResponseEntity
                    .ok(
                        addressService.searchAddresses(
                                criteria,
                                (clientId.isEmpty() ? authToken.getRole() : UserRole.CLIENT),
                                clientId.orElse(authToken.getUserId()),
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

    /**
     * Fetch a default address.
     * If the user is a customer or broker, we should be able to fetch their children's addresses too.
     * If no clientId is provided look up the currently logged in user
     * If no address type is provided lookup sender addess
     * @param addressType SENDER or DELIVERY will return SENDER by default
     * @param clientId the id of a clientId to lookup, if not the current user
     * @param authentication Spring provided authentication object
     * @return An Address DTO containing the Sender/Delivery default address
     */
    @RequestMapping(path="/default", method = RequestMethod.GET)
    public ResponseEntity<AddressDTO> readDefaultAddress(@RequestParam Optional<AddressType> addressType,
                                                         @RequestParam Optional<Long> clientId,
                                                         Authentication authentication) {

        // Get the User ID of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        try {
            // check if the optional clientId is provided. If so, check the caller is the client /is a parent of the client
            if(clientId.isPresent() && authToken.getUserId() != clientId.get()) {

                List<Long> children = userService
                        .getChildren(authToken.getUserId())
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList());

                if (children.isEmpty() || !children.contains(clientId.get())){
                    ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
            }


            // perform read to address repository by calling the address book service
            Address address = addressService
                    .getDefaultAddress(
                            clientId.orElse(authToken.getUserId()),
                            (clientId.isEmpty() ? authToken.getRole() : UserRole.CLIENT),
                            addressType.orElse(AddressType.SENDER)
                    );

            if (Objects.isNull(address)) {
                // Return 204 if zero result
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }

            return ResponseEntity.ok(AddressDTO.fromAddress(address));

        } catch (DataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
