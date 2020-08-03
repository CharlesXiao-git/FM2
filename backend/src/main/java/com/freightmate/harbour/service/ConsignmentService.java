package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.AddressNotFoundException;
import com.freightmate.harbour.exception.BadRequestException;
import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.ConsignmentDto;
import com.freightmate.harbour.repository.ConsignmentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsignmentService {

    private final ConsignmentRepository consignmentRepository;
    private final AddressService addressService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final Logger LOG = LoggerFactory.getLogger(ConsignmentService.class);

    ConsignmentService(@Autowired ConsignmentRepository consignmentRepository,
                   @Autowired AddressService addressService,
                   @Autowired UserDetailsService userDetailsService,
                   @Autowired UserService userService,
                   @Autowired ModelMapper modelMapper) {
        this.consignmentRepository = consignmentRepository;
        this.addressService = addressService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
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

        // Set the delivery & sender address details to consignment
        setConsignmentAddresses(delivery.get(), sender.get(), newConsignment);

        // Save consignment after the user id has been assigned
        setConsignmentUser(user, newConsignment);
        return consignmentRepository.save(newConsignment);
    }

    // Read
    public ConsignmentQueryResult readConsignment(String username, Pageable pageable) {
        // find user by username
        User user = userDetailsService.loadUserByUsername(username);

        // Find consignments under the user
        List<Consignment> consignments = consignmentRepository.findConsignments(user.getId(), pageable);

        return ConsignmentQueryResult.builder()
                .count(consignments.size())
                .consignments(consignments)
                .build();
    }

    // Update
    public Consignment updateConsignment(ConsignmentDto consignmentRequest, String requestorUsername) {
        // Validate the consignment to be updated exists
        Optional<Consignment> currentConsignment = this.getConsignments(
                Collections.singletonList(consignmentRequest.getId())
        ).stream().findFirst();

        if (currentConsignment.isEmpty()) {
            throw new BadRequestException("Unable to find consignment");
        }

        // Validate if user has permission to update
        // Return 403 if the requestor is a client and the consignment does not belong to the user

        User user = userDetailsService.loadUserByUsername(requestorUsername);

        if(user.getUserRole().equals(UserRole.CLIENT) && user.getId() != currentConsignment.get().getClientId()) {
            throw new ForbiddenException("User does not have permission to update this consignment");
        }

        // If the user is not a client then validate the children user under the requestor owns the consignment
        boolean userOwnsOrIsParent = userService
                .getChildren(requestorUsername)
                .stream()
                .anyMatch(child -> child.getId() == currentConsignment.get().getClientId());

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

        // Map the updated values into the existing consignment object
        // Null will be ignored and treated as no update
        modelMapper.map(consignmentRequest, currentConsignment);

        // Save updated consignment
        return consignmentRepository.save(currentConsignment.get());
    }

    // Delete
    @Transactional
    public void deleteConsignment(List<Long> ids, String username) {
        // Get user details from username
        User user = userDetailsService.loadUserByUsername(username);

        // Get a list of existing consignments
        List<Consignment> consignments = this.getConsignments(ids);
        if (consignments.size() != ids.size()) {
            throw new BadRequestException("Unable to find all consignments");
        }

        // Validate the the user is the parent or the owner of the consignments
        List<Long> children = userService
                .getChildren(username)
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
        consignmentRepository.deleteConsignments(ids, user.getId());
    }

    public List<Consignment> getConsignments(List<Long> ids) {
        return consignmentRepository.findConsignments(ids);
    }

    private void setConsignmentUser(User user, Consignment consignment) {
        consignment.setClient(user);
        consignment.setClientId(user.getId());
    }

    private void setConsignmentAddresses(Address delivery, Address sender, Consignment consignment) {
        consignment.setDeliveryAddress(delivery);
        consignment.setSenderAddress(sender);
    }
}