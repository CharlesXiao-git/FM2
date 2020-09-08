package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.CarrierAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CarrierAccountRepository extends PagingAndSortingRepository<CarrierAccount, Long> {
    List<CarrierAccount> findAllByUserBrokerId(long userBrokerId);
}
