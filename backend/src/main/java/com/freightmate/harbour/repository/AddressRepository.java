package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressType;
import com.freightmate.harbour.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
    String ADDRESS_BY_ID = "SELECT * FROM address a " +
            "WHERE a.id = ?1 " +
            "AND a.address_type = ?2";

    String CLIENT_ADDRESSES = "SELECT * FROM address a " +
            "WHERE a.client_id = ?1 " +
            "AND a.address_type = ?2";

    String CUSTOMER_ADDRESSES = "SELECT * FROM address a " +
            "WHERE a.customer_id = ?1" +
            "AND a.address_type = 'DELIVERY'"; // Only delivery addresses have reference to customer_id

    String CLIENT_ADDRESS_LINE1_LIKE = "SELECT * FROM address a " +
            "WHERE a.address_line_1 LIKE '%?1%' " +
            "AND a.address_type = ?2 " +
            "AND a.client_id = ?3";

    String CUSTOMER_ADDRESS_LINE1_LIKE = "SELECT * FROM address a " +
            "WHERE a.address_line_1 LIKE '%?1%' " +
            "AND a.address_type = 'DELIVERY' " + // Only delivery addresses have reference to customer_id
            "AND a.client_id = ?3";

    @Query(value = ADDRESS_BY_ID)
    Address findDeliveryAddressById(Long id, String addressType);

    @Query(value = CLIENT_ADDRESSES)
    List<Address> findAddressesByClient(String clientId, String addressType);

    @Query(value = CUSTOMER_ADDRESSES)
    List<Address> findAddressesByCustomer(String customerId);

    @Query(value = CLIENT_ADDRESS_LINE1_LIKE)
    List<Address> findClientAddressByAddressLine1Like(String query, String addressType, Long client_id);

    @Query(value = CUSTOMER_ADDRESS_LINE1_LIKE)
    List<Address> findCustomerAddressByAddressLine1Like(String query, String addressType, Long customer_id);

}
