package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.AddressNotFoundException;
import com.freightmate.harbour.exception.BadRequestException;
import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.ConsignmentDTO;
import com.freightmate.harbour.model.dto.ItemDTO;
import com.freightmate.harbour.repository.ConsignmentRepository;
import com.freightmate.harbour.repository.ItemTypeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsignmentService {

    private final ConsignmentRepository consignmentRepository;
    private final AddressService addressService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final ItemTypeRepository itemTypeRepository;
    private final ModelMapper modelMapper;
    private static final Logger LOG = LoggerFactory.getLogger(ConsignmentService.class);

    ConsignmentService(@Autowired ConsignmentRepository consignmentRepository,
                   @Autowired AddressService addressService,
                   @Autowired UserDetailsService userDetailsService,
                   @Autowired UserService userService,
                   @Autowired ItemTypeRepository itemTypeRepository,
                   @Autowired ModelMapper modelMapper) {
        this.consignmentRepository = consignmentRepository;
        this.addressService = addressService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.itemTypeRepository = itemTypeRepository;
        this.modelMapper = modelMapper;
    }

    // Create
    public Consignment createConsignment(Consignment newConsignment, String username) {
        // Get user details from username
        User user = userDetailsService.loadUserByUsername(username);

        // Get delivery address details
        Optional<Address> delivery = addressService.getAddresses(
                Collections.singletonList(
                        newConsignment.getDeliveryAddressId()
                )
        ).stream().findFirst();

        // Get sender address details
        Optional<Address> sender = addressService.getAddresses(
                Collections.singletonList(
                        newConsignment.getSenderAddressId()
                )
        ).stream().findFirst();

        // Validate that the delivery & sender addresses are valid
        if(delivery.isEmpty() || sender.isEmpty()) {
            LOG.error("Unable to find delivery and/or sender address for the consignment.");
            throw new AddressNotFoundException("Delivery, sender or both addresses are not valid");
        }

        // Validate that consignment has items
        if (Objects.isNull(newConsignment.getItems()) ||  newConsignment.getItems().size() == 0) {
            LOG.error("At least one item is required to create a consignment.");
            throw new BadRequestException("At least one item is required to create a consignment.");
        }

        // Set the delivery & sender address details to consignment
        setConsignmentAddresses(delivery.get(), sender.get(), newConsignment);

        // Save consignment after the user id has been assigned
        setConsignmentUser(user, newConsignment);

        /*
         * todo:
         *  validate the items (i.e. total weight, volume, max length, etc.).
         *  if it isn't mutable we dont need to validate the dimension fields against the value in the database table.
         *    But do a validation on the dimension calculation
         *  if it's mutable then need to check the calculation is correct (do a simple calculation)
         */

        //set consignment for all the items
        List<Item> requestItems = newConsignment.getItems();
        for(Item item : requestItems) {
            item.setConsignment(newConsignment);
        }

        return consignmentRepository.save(newConsignment);
    }

    // Read
    public List<Consignment> readConsignment(long userId, Pageable pageable) {
        // Find consignments under the user
        return consignmentRepository.findConsignments(userId, pageable);
    }

    // Update
    public void updateConsignment(ConsignmentDTO consignmentRequest, UserRole userRole, long userId) {
        // Validate the consignment to be updated exists
        Optional<Consignment> currentConsignment = this.getConsignments(
                Collections.singletonList(consignmentRequest.getId())
        ).stream().findFirst();

        if (currentConsignment.isEmpty()) {
            throw new BadRequestException("Unable to find consignment");
        }

        Consignment con = currentConsignment.get();

        // Validate if user has permission to update
        // Return 403 if the requestor is a client and the consignment does not belong to the user
        if(userRole.equals(UserRole.CLIENT) && userId != currentConsignment.get().getClientId()) {
            throw new ForbiddenException("User does not have permission to update this consignment");
        }

        // If the user is not a client then validate the children user under the requestor owns the consignment
        boolean userOwnsOrIsParent = userService
                .getChildren(userId,true)
                .stream()
                .anyMatch(child -> child.getId() == con.getClientId());

        if(!userOwnsOrIsParent) {
            throw new ForbiddenException("User does not have permission to update this consignment");
        }

        // Validate delivery and sender addresses are valid
        Optional<Address> delivery = addressService.getAddresses(
                Collections.singletonList(
                        consignmentRequest.getDeliveryAddressId()
                )
        ).stream().findFirst();

        Optional<Address> sender = addressService.getAddresses(
                Collections.singletonList(
                        consignmentRequest.getSenderAddressId()
                )
        ).stream().findFirst();

        if(delivery.isEmpty() || sender.isEmpty()) {
            LOG.error("Unable to find delivery and/or sender address for the consignment.");
            throw new AddressNotFoundException("Delivery, sender or both addresses are not valid");
        }

        // Filter the existing consignment items to get the items as per the request
        // This is for deleted items and to ensure that we filter out the deleted items from the existing consignment
        List<Long> requestItemIds = consignmentRequest
                .getItems()
                .stream()
                .map(ItemDTO::getId)
                .collect(Collectors.toList());
        List<Item> requestItems = con
                .getItems()
                .stream()
                .filter(item -> requestItemIds.contains(item.getId()))
                .collect(Collectors.toList());

        // Set the filtered items as the existing consignment items
        con.setItems(requestItems);

        // Map the updated values into the existing consignment object
        // Null will be ignored and treated as no update
        modelMapper.map(consignmentRequest, con);

        // Make sure that every item in the consignment knows their parent consignment
        for(Item item : con.getItems()) {
            item.setConsignment(con);
        }

        // Save updated consignment
        consignmentRepository.save(con);
    }

    // Delete
    @Transactional
    public void deleteConsignment(List<Long> ids, long userId) {
        // Get a list of existing consignments
        List<Consignment> consignments = this.getConsignments(ids);
        if (consignments.size() != ids.size()) {
            throw new BadRequestException("Unable to find all consignments");
        }

        // Validate the the user is the parent or the owner of the consignments
        List<Long> children = userService
                .getChildren(userId, true)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Consignment> consignmentsCanDelete = consignments.stream()
                .filter(consignment -> children.contains(consignment.getClientId()))
                .collect(Collectors.toList());

        if (consignmentsCanDelete.size() != consignments.size()) {
            throw new ForbiddenException("User does not have permission to delete provided consignments");
        }

        // Perform delete on the consignment IDs
        consignmentRepository.deleteConsignments(ids, userId);
    }

    public List<Consignment> getConsignments(List<Long> ids) {
        return consignmentRepository.findConsignments(ids);
    }

    private void setConsignmentUser(User user, Consignment consignment) {
        consignment.setClient(user);
        consignment.setClientId(user.getId());
    }

    private void setConsignmentAddresses(Address delivery, Address sender, Consignment consignment) {
        consignment.setDeliveryAddressId(delivery.getId());
        consignment.setSenderAddressId(sender.getId());
    }
}
