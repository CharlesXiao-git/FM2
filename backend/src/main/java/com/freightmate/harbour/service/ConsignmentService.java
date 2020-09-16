package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.AddressNotFoundException;
import com.freightmate.harbour.exception.BadRequestException;
import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.helper.ListHelper;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.AddressDTO;
import com.freightmate.harbour.model.dto.ConsignmentDTO;
import com.freightmate.harbour.model.dto.ItemDTO;
import com.freightmate.harbour.repository.ConsignmentRepository;
import com.freightmate.harbour.repository.OfferRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsignmentService {

    private final ConsignmentRepository consignmentRepository;
    private final AddressService addressService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final ItemTypeService itemTypeService;
    private final ModelMapper modelMapper;
    private final OfferRepository offerRepository;
    @PersistenceContext private final EntityManager entityManager;
    private static final Logger LOG = LoggerFactory.getLogger(ConsignmentService.class);

    ConsignmentService(@Autowired ConsignmentRepository consignmentRepository,
                   @Autowired AddressService addressService,
                   @Autowired UserDetailsService userDetailsService,
                   @Autowired UserService userService,
                   @Autowired ItemTypeService itemTypeService,
                   @Autowired ModelMapper modelMapper,
                   @Autowired OfferRepository offerRepository,
                   @Autowired EntityManagerFactory emf) {
        this.consignmentRepository = consignmentRepository;
        this.addressService = addressService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.itemTypeService = itemTypeService;
        this.modelMapper = modelMapper;
        this.offerRepository = offerRepository;
        this.entityManager = emf.createEntityManager();
    }

    // Create
    public Consignment createConsignment(Consignment newConsignment, long userId) {
        // Get user details from user ID
        User user = userDetailsService.getFirst(userId);

        // Get delivery address details
        Address delivery = addressService.getFirst(
                newConsignment.getDeliveryAddressId()
        );

        // Get sender address details
        Address sender = addressService.getFirst(
                newConsignment.getSenderAddressId()
        );

        // Validate that the delivery & sender addresses are valid
        if(Objects.isNull(delivery) || Objects.isNull(sender)) {
            LOG.error("Unable to find delivery and/or sender address for the consignment.");
            throw new AddressNotFoundException("Delivery, sender or both addresses are not valid");
        }

        // Validate that consignment has items
        if (Objects.isNull(newConsignment.getItems()) ||  newConsignment.getItems().size() == 0) {
            LOG.error("At least one item is required to create a consignment.");
            throw new BadRequestException("At least one item is required to create a consignment.");
        }

        // Set the delivery & sender address details to consignment
        setConsignmentAddresses(delivery, sender, newConsignment);

        // Save consignment after the user id has been assigned
        setConsignmentUser(user, newConsignment);

        // For each item we need to get the actual item type object by querying the DB using all item's itemTypeId
        // First we get all the itemTypeId from the request
        List<Long> itemTypeIds = newConsignment
                .getItems()
                .stream()
                .map(Item::getItemTypeId)
                .collect(Collectors.toList());
        // Then create a map of ItemType having its ID as the key
        Map<Long, ItemType> itemTypes = this.getConsignmentItemTypes(itemTypeIds);

        // set consignment for all the items and set the ItemType
        List<Item> requestItems = newConsignment.getItems();
        for(Item item : requestItems) {
            item.setConsignment(newConsignment);
            item.setItemType(itemTypes.get(item.getItemTypeId()));
        }

        return consignmentRepository.save(newConsignment);
    }

    // Read
    public List<Consignment> readConsignment(long userId, UserRole userRole, Pageable pageable) {
        // Find consignments under the user
        return consignmentRepository.findConsignments(userRole.name(), userId, pageable);
    }

    // Update
    public void updateConsignment(ConsignmentDTO consignmentRequest, UserRole userRole, long userId) {
        // Validate the consignment to be updated exists
        Consignment currentConsignment = this.getFirst(consignmentRequest.getId());

        // Validate that the updated consignment has the same Owner ID as the current consignemt Owner ID
        if (consignmentRequest.getOwnerId() != currentConsignment.getUserClient().getUserId()) {
            throw new ForbiddenException("Owner ID of existing consignment cannot be changed");
        }

        // Validate if user has permission to update
        // Return 403 if the requestor is a client and the consignment does not belong to the user
        if(userRole.equals(UserRole.CLIENT) && userId != currentConsignment.getUserClient().getUserId()) {
            throw new ForbiddenException("User does not have permission to update this consignment");
        }

        // If the user is not a client then validate the children user under the requestor owns the consignment
        boolean userOwnsOrIsParent = userService
                .getChildren(userId,true)
                .stream()
                .anyMatch(child -> child.getId() == currentConsignment.getUserClient().getUserId());

        if(!userOwnsOrIsParent) {
            throw new ForbiddenException("User does not have permission to update this consignment");
        }

        // Validate delivery and sender addresses are valid
        // Get delivery address details
        Address delivery = addressService.getFirst(
                consignmentRequest.getDeliveryAddressId()
        );

        // Get sender address details
        Address sender = addressService.getFirst(
                consignmentRequest.getSenderAddressId()
        );

        if(Objects.isNull(delivery) || Objects.isNull(sender)) {
            LOG.error("Unable to find delivery and/or sender address for the consignment.");
            throw new AddressNotFoundException("Delivery, sender or both addresses are not valid");
        }

        // Set the delivery & sender address details to consignment
        setConsignmentAddresses(delivery,
                sender,
                consignmentRequest);

        // Filter the existing consignment items to get the items as per the request
        // This is for deleted items and to ensure that we filter out the deleted items from the existing consignment
        List<Long> requestItemIds = consignmentRequest
                .getItems()
                .stream()
                .map(ItemDTO::getId)
                .collect(Collectors.toList());
        List<Item> requestItems = currentConsignment
                .getItems()
                .stream()
                .filter(item -> requestItemIds.contains(item.getId()))
                .collect(Collectors.toList());

        // For each item we need to get the actual item type object by querying the DB using all item's itemTypeId
        // First we get all the itemTypeId from the request
        List<Long> itemTypeIds = consignmentRequest
                .getItems()
                .stream()
                .map(ItemDTO::getItemTypeId)
                .collect(Collectors.toList());
        // Then create a map of ItemType having its ID as the key
        // Not querying multiple times but instead collating all the item type IDs and querying the item_type table once with all the IDs
        Map<Long, ItemType> itemTypes = this.getConsignmentItemTypes(itemTypeIds);

        // Set the filtered items as the existing consignment items
        currentConsignment.setItems(requestItems);

        // Clear existing consignment entity from persistence context
        // This tells Spring to forget about any existing address details so that we can ensure that spring
        // set the right addresses instead of thinking that we are trying to change the address ID
        this.entityManager.detach(currentConsignment);
        this.entityManager.detach(currentConsignment.getSenderAddress());
        this.entityManager.detach(currentConsignment.getDeliveryAddress());
        this.entityManager.detach(currentConsignment.getSenderAddress().getSuburb());
        this.entityManager.detach(currentConsignment.getDeliveryAddress().getSuburb());
        // Uncomment below to clear all persistence object which is not recommended
        // clear() will force spring to query all related objects and set it into persistence context which will add more processing time
//        this.entityManager.clear();

        // Map the updated values into the existing consignment object
        // Null will be ignored and treated as no update
        modelMapper.map(consignmentRequest, currentConsignment);

        // Make sure that every item in the consignment knows their parent consignment and set the ItemType
        for(Item item : currentConsignment.getItems()) {
            item.setConsignment(currentConsignment);
            item.setItemType(itemTypes.get(item.getItemTypeId()));
        }

        // Save updated consignment
        consignmentRepository.save(currentConsignment);
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
                .filter(consignment -> children.contains(consignment.getUserClient().getUserId()))
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

    public Consignment getFirst(long consignmentId) {
        return ListHelper.getFirst(
                this.getConsignments(
                        Collections.singletonList(consignmentId)
                )
        );
    }

    public void createOffer(long offerId, long consignmentId) {
        Offer offer = ListHelper.getFirst(offerRepository.getOffersByIds(Collections.singletonList(offerId)));

        Consignment consignment = ListHelper.getFirst(this.getConsignments(Collections.singletonList(consignmentId)));
        this.entityManager.detach(offer);
        offer.setId(0);
        offer.setConsignment(consignment);
        offer.setSelected(true);
        offerRepository.save(offer);
    }

    private void setConsignmentUser(User user, Consignment consignment) {
        consignment.setUserClient(user.getUserClient());
        consignment.setUserClientId(user.getId());
    }

    private void setConsignmentAddresses(Address delivery, Address sender, Consignment consignment) {
        consignment.setDeliveryAddress(delivery);
        consignment.setDeliveryAddressId(delivery.getId());
        consignment.setSenderAddressId(sender.getId());
        consignment.setSenderAddress(sender);
    }

    private void setConsignmentAddresses(Address delivery, Address sender, ConsignmentDTO consignment) {
        consignment.setDeliveryAddress(AddressDTO.fromAddress(delivery));
        consignment.setDeliveryAddressId(delivery.getId());
        consignment.setSenderAddressId(sender.getId());
        consignment.setSenderAddress(AddressDTO.fromAddress(sender));
    }

    private Map<Long, ItemType> getConsignmentItemTypes(List<Long> itemTypeIds) {
        // Get all the ItemType objects using the IDs from previous step
        List<ItemType> itemTypes = itemTypeService.getItemTypes(itemTypeIds);
        // Create a Map of ItemType having the id as the key
        return itemTypes
                .stream()
                .collect(
                        Collectors.toMap(
                                ItemType::getId,
                                itemType -> itemType
                        )
                );
    }
}
