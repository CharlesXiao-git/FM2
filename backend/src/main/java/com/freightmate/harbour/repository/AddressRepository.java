package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long>{
    String ADDRESS_BY_IDS = "SELECT a.id, a.user_client_id, a.user_customer_id, a.user_broker_id, a.address_type, a.reference_id, a.company, a.address_line_1,  \n" +
            "       a.suburb_id, a.address_line_2, a.contact_name, a.phone_number, a.email, a.special_instructions, \n" +
            "       a.is_default, a.is_deleted, a.deleted_at, a.created_at, a.updated_at, a.deleted_by, a.created_by, a.updated_by\n" +
            "FROM address a\n" +
            "WHERE a.id IN ?1\n" +
            "AND a.is_deleted = FALSE";

    // The following query is to perform a search to the address details. The search will be performed to a combination
    // of fields from address tables (a.reference_id, a.contact_name, a.company_name, a.address_line_1, a.address_line_2,
    // a.town, a.postcode, a.state)
    //
    // The address table is also joined to user table so that we get the hierarchy of the user. We use client as it is
    // the lowest level. The query has case statement to check the user role (?2), depending on the user role we use
    // the respective ID field to get the addresses under that ID
    //
    // Lastly after we got all the addresses, we perform the search using LIKE condition on the concatenated fields
    String ADDRESS_LIKE = "SELECT x.id, x.user_client_id, x.user_customer_id, x.user_broker_id, x.address_type, x.reference_id, x.company, x.address_line_1, \n" +
            "                   x.suburb_id, x.address_line_2, x.contact_name, x.phone_number, x.email, x.special_instructions, \n" +
            "                   x.is_default, x.is_deleted, x.deleted_at, x.created_at, x.updated_at, x.deleted_by, x.created_by, x.updated_by\n" +
            "FROM (\n" +
            "         SELECT a.*,\n" +
            "                CONCAT_WS('', COALESCE(a.reference_id, ''), a.contact_name, a.company, a.address_line_1,\n" +
            "                          a.address_line_2, s.name,\n" +
            "                          s.postcode, s.state) concat_address_details\n" +
            "         FROM address a\n" +
            "                  INNER JOIN suburb s on a.suburb_id = s.id\n" +
            "                  LEFT JOIN user_client cli ON a.user_client_id = cli.id\n" +
            "                  LEFT JOIN user_customer cus ON a.user_customer_id = cus.id\n" +
            "                  LEFT JOIN user_broker bro ON cus.user_broker_id = bro.id\n" +
            "                  LEFT JOIN user_client curr ON curr.user_id = ?3\n" +
            "         WHERE a.is_deleted = FALSE\n" +
            "           AND CASE\n" +
            "                   WHEN ?1 = 'ANY' THEN a.address_type IN ('DELIVERY', 'SENDER')\n" +
            "                   ELSE a.address_type = ?1\n" +
            "             END\n" +
            "           AND CASE\n" +
            "                   WHEN ?2 = 'BROKER' THEN ?3 = bro.user_id\n" + // check if I'm the broker for it
            "                   WHEN ?2 = 'CUSTOMER' THEN ?3 = cus.user_id\n" + // cases when the row is the customer's, or when the customer is the parent of the owner
            "                   WHEN ?2 = 'CLIENT' THEN a.user_customer_id = curr.user_customer_id\n" + // if I'm a client, and theres a row without a client id, check if my customer owns it
            "                   ELSE ?3 = cli.user_id\n" + // otherwise check if its mine
            "             END\n" +
            "     ) AS x\n" +
            "WHERE LOWER(x.concat_address_details) LIKE LOWER(CONCAT('%', ?4, '%'))";

    String ADDRESS_BY_USERID = "SELECT a.id, a.user_client_id, a.user_customer_id, a.user_broker_id, a.address_type, a.reference_id, a.company, a.address_line_1,  \n" +
            "       a.suburb_id, a.address_line_2, a.contact_name, a.phone_number, a.email, a.special_instructions, \n" +
            "       a.is_default, a.is_deleted, a.deleted_at, a.created_at, a.updated_at, a.deleted_by, a.created_by, a.updated_by\n" +
            "FROM address a\n" +
            "         LEFT JOIN user_client cli ON a.user_client_id = cli.id\n" +
            "         LEFT JOIN user_customer cus ON a.user_customer_id = cus.id\n" +
            "         LEFT JOIN user_broker bro ON cus.user_broker_id = bro.id\n" +
            "         LEFT JOIN user_client curr ON curr.user_id = ?3\n" +
            "WHERE a.is_deleted = FALSE\n" +
            "  AND CASE\n" +
            "          WHEN ?1 = 'ANY' THEN a.address_type IN ('DELIVERY', 'SENDER')\n" +
            "          ELSE a.address_type = ?1\n" +
            "    END\n" +
            "  AND CASE\n" +
            "          WHEN ?2 = 'BROKER' THEN ?3 = bro.user_id\n" + // check if I'm the broker for it
            "          WHEN ?2 = 'CUSTOMER' THEN ?3 = cus.user_id\n" + // cases when the row is the customer's, or when the customer is the parent of the owner
            "          WHEN ?2 = 'CLIENT' THEN a.user_customer_id = curr.user_customer_id\n" + // if I'm a client, and theres a row without a client id, check if my customer owns it
            "          ELSE ?3 = cli.user_id\n" + // otherwise check if its mine
            "    END";

    String DELETE_BY_IDS = "UPDATE address a\n" +
            "LEFT JOIN user_client cli ON a.user_client_id = cli.id\n" +
            "LEFT JOIN user_customer cus ON a.user_customer_id = cus.id\n" +
            "LEFT JOIN user_broker bro ON cus.user_broker_id = bro.id\n" +
            "LEFT JOIN user_client curr ON curr.user_id = ?3\n" +
            "SET\n" +
            "a.is_deleted = TRUE,\n" +
            "a.deleted_at = NOW(),\n" +
            "a.deleted_by = ?3\n" +
            "WHERE a.id IN ?1\n" +
            "AND a.is_deleted = FALSE\n" +
            "AND CASE\n" +
            "          WHEN ?2 = 'BROKER' THEN ?3 = bro.user_id\n" + // check if I'm the broker for it
            "          WHEN ?2 = 'CUSTOMER' THEN ?3 = cus.user_id\n" + // cases when the row is the customer's, or when the customer is the parent of the owner
            "          WHEN ?2 = 'CLIENT' THEN a.user_customer_id = curr.user_customer_id\n" + // if I'm a client, and theres a row without a client id, check if my customer owns it
            "          ELSE ?3 = cli.user_id\n" + // otherwise check if its mine
            "    END";

    String USER_ADDRESSES = "SELECT a.id, a.user_client_id, a.user_customer_id, a.user_broker_id, a.address_type, a.reference_id, a.company, a.address_line_1,  \n" +
            "       a.suburb_id, a.address_line_2, a.contact_name, a.phone_number, a.email, a.special_instructions, \n" +
            "       a.is_default, a.is_deleted, a.deleted_at, a.created_at, a.updated_at, a.deleted_by, a.created_by, a.updated_by\n" +
            "FROM address a\n" +
            "         LEFT JOIN user_client cli ON a.user_client_id = cli.id\n" +
            "         LEFT JOIN user_customer cus ON a.user_customer_id = cus.id\n" +
            "         LEFT JOIN user_broker bro ON cus.user_broker_id = bro.id\n" +
            "         LEFT JOIN user_client curr ON curr.user_id = ?3\n" +
            "WHERE a.is_deleted = FALSE\n" +
            "AND CASE\n" +
            "          WHEN ?2 = 'BROKER' THEN ?3 = bro.user_id\n" + // check if I'm the broker for it
            "          WHEN ?2 = 'CUSTOMER' THEN ?3 = cus.user_id\n" + // cases when the row is the customer's, or when the customer is the parent of the owner
            "          WHEN ?2 = 'CLIENT' THEN a.user_customer_id = curr.user_customer_id\n" + // if I'm a client, and theres a row without a client id, check if my customer owns it
            "          ELSE ?3 = cli.user_id\n" + // otherwise check if its mine
            "    END\n" +
            "AND a.id IN ?1";

    @Query(value = ADDRESS_BY_IDS, nativeQuery = true)
    List<Address> findAddresses(List<Long> ids);

    // Get all addresses under the user ID (client or customer)
    @Query(value = ADDRESS_BY_USERID, nativeQuery = true)
    List<Address> findAddresses(String addressType, String userRole, long userId, Pageable pageable);

    @Modifying
    @Query(value = DELETE_BY_IDS, nativeQuery = true)
    Integer deleteAddressesByIds(List<Long> addressIds, String userRole, long userId);

    @Query(value = ADDRESS_LIKE, nativeQuery = true)
    List<Address> findAddresses(String addressType, String userRole, long userId, String criteria);

    @Query(value = USER_ADDRESSES, nativeQuery = true)
    List<Address> findAddresses(List<Long> addressIds, String userRole, long userId);
}
