package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.BadRequestException;
import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.helper.ListHelper;
import com.freightmate.harbour.model.Carrier;
import com.freightmate.harbour.model.CarrierAccount;
import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.model.dto.CarrierAccountDTO;
import com.freightmate.harbour.repository.CarrierAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class CarrierAccountService {
    private final CarrierAccountRepository carrierAccountRepository;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final CarrierService carrierService;
    private final ModelMapper modelMapper;
    @PersistenceContext private final EntityManager entityManager;

    CarrierAccountService(
            @Autowired CarrierAccountRepository carrierAccountRepository,
            @Autowired UserDetailsService userDetailsService,
            @Autowired UserService userService,
            @Autowired CarrierService carrierService,
            @Autowired ModelMapper modelMapper,
            @Autowired EntityManagerFactory emf
            ) {
        this.carrierAccountRepository = carrierAccountRepository;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.carrierService = carrierService;
        this.modelMapper = modelMapper;
        this.entityManager = emf.createEntityManager();
    }

    // Create
    public CarrierAccount createCarrierAccount(CarrierAccount carrierAccount, long userId) {
        // Get user details
        User user = userDetailsService.getFirst(userId);

        // Validate if the user is a broker or not
        if (!user.isUserBroker()) {
            throw new ForbiddenException("User does not have permission to create carrier account");
        }

        // Validate that the customer belongs to the broker if it's provided
        if (Objects.nonNull(carrierAccount.getUserCustomerId()) &&
                !userService.isChildOf(userId, carrierAccount.getUserCustomerId())) {
            throw new ForbiddenException("User does not have permission to create a carrier account for the customer");
        }

        // Validate that the carrier exists
        Carrier carrier = carrierService.getFirst(carrierAccount.getCarrierId());

        // Check if the Account Number is provided with empty string
        if (Objects.isNull(carrierAccount.getAccountNumber()) || carrierAccount.getAccountNumber().isBlank()) {
            throw new BadRequestException("Account number cannot be blank");
        }

        // Set the broker userId to the carrier account record
        carrierAccount.setUserBroker(
                user
                .getUserBroker()
        );

        // Set the customer userId to the carrier account record if it's provided and exists
        if (Objects.nonNull(carrierAccount.getUserCustomerId())) {
            User customer = userDetailsService.getFirst(carrierAccount.getUserCustomerId());
            if (!customer.getUserRole().equals(UserRole.CUSTOMER)) {
                throw new ForbiddenException("User Customer ID provided with a user that is not a Customer. Please provide a Customer instead.");
            }
            carrierAccount.setUserCustomer(
                    customer
                            .getUserCustomer()
            );
        }

        // Set the carrier to the request
        carrierAccount.setCarrier(carrier);

        // Save new Carrier Account
        return carrierAccountRepository.save(carrierAccount);
    }

    // Read
    public List<CarrierAccount> readCarrierAccount (long userId) {
        return carrierAccountRepository.getCarrierAccounts(userId);
    }

    // Update
    public void updateCarrierAccount (CarrierAccountDTO carrierAccountDTO, long userId) {
        // Validate that the carrier account to be updated exists
        CarrierAccount currentCarrierAccount = this.getFirst(carrierAccountDTO.getId());

        // Validate that the carrier exists
        Carrier carrier = carrierService.getFirst(carrierAccountDTO.getCarrierId());

        // Validate that the customer belongs to the broker
        if (Objects.nonNull(carrierAccountDTO.getUserCustomerId()) && !userService.isChildOf(userId, carrierAccountDTO.getUserCustomerId())) {
            throw new ForbiddenException("User does not have permission to update a carrier account for the customer");
        }

        // Clear existing Carrier Account and other entities from persistence context
        this.entityManager.detach(currentCarrierAccount);
        // Only clear Carrier if it's updated
        if(!carrierAccountDTO.getCarrierId().equals(currentCarrierAccount.getCarrierId())) {
            this.entityManager.detach(currentCarrierAccount.getCarrier());
            currentCarrierAccount.setCarrier(carrier);
        }
        //Only clear UserCustomer if it's updated
        if(Objects.nonNull(carrierAccountDTO.getUserCustomerId()) &&
                !carrierAccountDTO.getUserCustomerId().equals(currentCarrierAccount.getUserCustomerId())
        ) {
            // Since there is a possibility for the current carrier account has no customer populated
            // We don't need to perform a detach if there is no instance of UserCustomer
            if(Objects.nonNull(currentCarrierAccount.getUserCustomer())) {
                this.entityManager.detach(currentCarrierAccount.getUserCustomer());
            }
            User customer = userDetailsService.getFirst(carrierAccountDTO.getUserCustomerId());
            if (!customer.getUserRole().equals(UserRole.CUSTOMER)) {
                throw new ForbiddenException("User Customer ID provided with a user that is not a Customer. Please provide a Customer instead.");
            }
            currentCarrierAccount.setUserCustomer(
                    customer
                            .getUserCustomer()
            );
        }

        // Perform comparison & mapping between the request against existing CarrierAccount\
        modelMapper.map(carrierAccountDTO, currentCarrierAccount);

        // Save the update
        carrierAccountRepository.save(currentCarrierAccount);
    }

    // Delete
    @Transactional
    public void deleteCarrierAccount(List<Long> ids, long userId) {
        // Check that all carrier accounts to be deleted exists under the current logged in broker
        List<CarrierAccount> carrierAccounts = carrierAccountRepository.getCarrierAccounts(userId, ids);
        if (carrierAccounts.size() != ids.size()) {
            throw new BadRequestException("Unable to find one or more carrier accounts to be deleted");
        }

        // Perform a delete
        carrierAccountRepository.deleteCarrierAccounts(ids, userId);
    }

    public CarrierAccount getFirst(long carrierAccountId) {
        return ListHelper.getFirst(
                carrierAccountRepository.getCarrierAccounts(
                        Collections.singletonList(carrierAccountId)
                )
        );
    }
}
