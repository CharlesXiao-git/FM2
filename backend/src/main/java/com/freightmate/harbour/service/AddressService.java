package com.freightmate.harbour.service;

import com.freightmate.harbour.model.dto.AddressDto;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserDetailsService userDetailsService;
    private static final Logger LOG = LoggerFactory.getLogger(AddressService.class);
    private final ModelMapper modelMapper;

    AddressService(@Autowired AddressRepository addressRepository,
                   @Autowired UserDetailsService userDetailsService,
                   @Autowired ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
    }

    // Create
    public Address createAddress(Address newAddress, String username) {
        // Get user details from username
        User user = userDetailsService.loadUserByUsername(username);

        // Save address after the user id has been assigned
        return addressRepository.save(setUserIdToAddress(user, newAddress));
    }

    // Read
    public AddressQueryResult readAddress(String username, AddressType addressType, Pageable pageable) {
        // todo: Need to re-look at this function again when "shared" setting is enabled. Do Lookup by user id to get address (client or customer)
        // find user by username
        User user = userDetailsService.loadUserByUsername(username);

        // Find addresses under the user
        List<Address> addresses = addressRepository.findAddressesByUserId(user.getId(),
                addressType.name(), pageable
        );
        return AddressQueryResult.builder()
                .count(addresses.size())
                .addresses(addresses)
                .build();
    }

    // Update
    public Address updateAddress(AddressDto dto, Address currentAddress) {
        // Map the updated values into the existing address object
        // Null will be ignored and treated as no update
        modelMapper.map(dto, currentAddress);
        // Save address after the user id has been assigned
        return addressRepository.save(currentAddress);
    }

    // Delete
    public Integer deleteAddress(List<Long> addressIds, String username) {
        // Get user details from username
        User user = userDetailsService.loadUserByUsername(username);

        // Perform delete to the address
        return addressRepository.deleteAddressesByIds(addressIds, user.getId());
    }

    public List<Address> getAddresses(List<Long> addressIds) {
        return addressRepository.findAddressesByIds(addressIds);
    }

    // User ID will need to be assigned to the address
    // This function set the user ID to its field depending on the user role
    // A CLIENT will need to populate both client_id and customer_id
    // A CUSTOMER will need to populate the CUSTOMER_ID
    private Address setUserIdToAddress(User user, Address newAddress) {
        if(user.getUserRole().equals(UserRole.CLIENT)) {
            // The following codes are used for the purpose of returning the IDs that have been set to the new address
            newAddress.setClientId(user.getId());
            newAddress.setCustomerId(user.getCustomer().getId());
            // The IDs will be stored into the address table by using the following code
            newAddress.setClient(user);
            newAddress.setCustomer(user.getCustomer());
        } else if (user.getUserRole().equals(UserRole.CUSTOMER)) {
            // Similarly with the client, the first code is to return the ID as a response
            // where the following code is to store the ID in the address table
            newAddress.setCustomerId(user.getCustomer().getId());
            newAddress.setCustomer(user.getCustomer());
        }
        return newAddress;
    }
}
