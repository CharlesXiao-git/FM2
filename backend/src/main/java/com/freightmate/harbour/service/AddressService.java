package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.BadRequestException;
import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressType;
import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.model.dto.AddressDTO;
import com.freightmate.harbour.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final SuburbService suburbService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(AddressService.class);
    private final ModelMapper modelMapper;
    @PersistenceContext private final EntityManager entityManager;

    AddressService(@Autowired AddressRepository addressRepository,
                   @Autowired SuburbService suburbService,
                   @Autowired UserDetailsService userDetailsService,
                   @Autowired UserService userService,
                   @Autowired ModelMapper modelMapper,
                   @Autowired EntityManagerFactory emf) {
        this.addressRepository = addressRepository;
        this.suburbService = suburbService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.entityManager = emf.createEntityManager();
    }

    // Create
    public Address createAddress(Address newAddress, long userId) {
        // Get user details from the user ID
        Optional<User> user = userDetailsService.getUsers(
                Collections.singletonList(userId)
        ).stream().findFirst();

        if (user.isEmpty()) {
            throw new BadRequestException("Unable to find user");
        }

        if (user.get().getUserRole().equals(UserRole.BROKER)) {
            throw new ForbiddenException("Addresses cannot be created for a BROKER, please provide a customer or client ID");
        }

        // Perform an update to suburb
        suburbService.updateSuburb(newAddress.getSuburb());

        // Save address after the user id has been assigned
        setUserIdToAddress(user.get(), newAddress);
        return addressRepository.save(newAddress);
    }

    // Read
    public List<Address> readAddress(long userId, UserRole userRole, AddressType addressType, Pageable pageable) {
        // todo: Need to re-look at this function again when "shared" setting is enabled. Do Lookup by user id to get address (client or customer)

        // Find addresses under the user
        return addressRepository
                .findAddresses(
                        addressType.name(),
                        userRole.name(),
                        userId,
                        pageable
                );
    }

    public Address getDefaultAddress(long userId, UserRole userRole, AddressType addressType) {
        Optional<Address> defaultAddress = this
                .readAddress(
                        userId,
                        userRole,
                        addressType,
                        null
                )
                .stream()
                .filter(address ->  address.getIsDefault() && address.getUserClient().getUserId() == userId)
                .findFirst();

        if(defaultAddress.isEmpty()) {
            return null;
        }

        return defaultAddress.get();
    }

    // Update
    public Address updateAddress(AddressDTO dto, Address currentAddress, long userId) {
        // If the new Address object has different Client ID than the current address Client ID
        // Check that the Client ID is the child of the logged in user
        if(Objects.nonNull(dto.getUserClientId()) && !dto.getUserClientId().equals(currentAddress.getUserClient().getUserId())) {
            if(!userService.isChildOf(userId, dto.getUserClientId())) {
                throw new ForbiddenException("User does not have permission to update this address");
            } else {
                throw new ForbiddenException("Client ID for an address shouldn't be updated");
            }
        }

        // Clear existing address entity from persistence context
        // This tells Spring to forget about any existing suburb details so that we can ensure that spring
        // set the right suburb instead of thinking that we are trying to change the suburb ID
        this.entityManager.detach(currentAddress);
        this.entityManager.detach(currentAddress.getSuburb());

        // Map the updated values into the existing address object
        // Null will be ignored and treated as no update
        modelMapper.map(dto, currentAddress);

        // Save address after the user id has been assigned
        return addressRepository.save(currentAddress);
    }

    // Delete
    public Integer deleteAddresses(List<Long> addressIds, long userId, UserRole userRole) {
        // validate all the address Ids exist for the user and their children
        List<Address> addressesByIds = addressRepository.findAddresses(addressIds, userRole.name(), userId);
        if(addressIds.size() != addressesByIds.size()) {
            throw new ForbiddenException("One or more addresses are not allowed to be deleted by the current user");
        }

        // Perform delete to the address
        return addressRepository.deleteAddressesByIds(addressIds, userRole.name(), userId);
    }

    // Search address by criteria and current logged in user
    public List<Address> searchAddresses(String searchCriteria, UserRole userRole, long userId, AddressType addressType) {
        // Perform search
        return addressRepository.findAddresses(addressType.name(), userRole.name(), userId, searchCriteria);
    }

    public List<Address> getAddresses(List<Long> addressIds) {
        return addressRepository.findAddresses(addressIds);
    }

    // User ID will need to be assigned to the address
    // This function set the user ID to its field depending on the user role
    // A CLIENT will need to populate both user_client_id user_customer_id and user_broker_id
    // A CUSTOMER will need to populate the user_customer_id and user_broker_id
    private void setUserIdToAddress(User user, Address newAddress) {
        if(user.getUserRole().equals(UserRole.CLIENT)) {
            // The following codes are used for the purpose of returning the IDs that have been set to the new address
            newAddress.setUserClientId(user.getUserClient().getId());
            newAddress.setUserClient(user.getUserClient());
            newAddress.setUserCustomer(user.getUserClient().getUserCustomer());
            newAddress.setUserBroker(user.getUserClient().getUserCustomer().getUserBroker());
        } else if (user.getUserRole().equals(UserRole.CUSTOMER)) {
            // Similarly with the client, the first code is to return the ID as a response
            // where the following code is to store the ID in the address table
            newAddress.setUserCustomerId(user.getUserCustomer().getId());
            newAddress.setUserCustomer(user.getUserCustomer());
            newAddress.setUserCustomerId(user.getUserCustomer().getUserBroker().getId());
            newAddress.setUserBroker(user.getUserCustomer().getUserBroker());
        }
    }
}
