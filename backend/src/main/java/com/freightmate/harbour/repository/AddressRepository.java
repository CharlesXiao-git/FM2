package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
    String ADDRESS_BY_IDS = "SELECT a.id, a.address_type, a.customer_id, a.client_id, a.reference_id, a.company_name, a.address_line_1, " +
            "       a.address_line_2, a.town, a.postcode, a.country, a.state, a.contact_name, a.contact_no, " +
            "       a.contact_email, a.notes, a.is_default, a.count_used, a.is_deleted, a.deleted_at, a.created_at, " +
            "       a.updated_at, a.deleted_by, a.created_by, a.updated_by " +
            "FROM address a " +
            "WHERE a.id IN ?1 " +
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
    String ADDRESS_LINE1_LIKE = "SELECT x.id, x.client_id, x.customer_id, x.address_type, x.reference_id, x.company_name, x.address_line_1, " +
            "       x.address_line_2, x.town, x.postcode, x.country, x.state, x.contact_name, x.contact_no, " +
            "       x.contact_email, x.notes, x.is_default, x.count_used, x.is_deleted, x.deleted_at, x.created_at, " +
            "       x.updated_at, x.deleted_by, x.created_by, x.updated_by " +
            "FROM ( " +
            "SELECT a.*, " +
            "       CONCAT(COALESCE(a.reference_id, ''), a.contact_name, a.company_name, a.address_line_1, a.address_line_2, a.town, " +
            "           a.postcode, a.state) concat_address_details " +
            "FROM address a " +
            "         LEFT JOIN user cli ON a.client_id = cli.id " +
            "         LEFT JOIN user cus ON a.customer_id = cus.id " +
            "WHERE a.is_deleted = FALSE " +
            "  AND CASE " +
            "          WHEN ?1 = 'ANY' THEN a.address_type IN ('DELIVERY', 'SENDER') " +
            "          ELSE a.address_type = ?1 " +
            "    END " +
            "  AND CASE " +
            "          WHEN ?2 = 'CUSTOMER' THEN cus.id = ?3 " +
            "          WHEN ?2 = 'CLIENT' THEN cli.id = ?3 " +
            "          ELSE cli.broker_id = ?3 " + // User might be a broker
            "    END) AS x " +
            "WHERE x.concat_address_details LIKE CONCAT('%', ?4, '%');";

    String ADDRESS_BY_USERID = "SELECT a.id, a.client_id, a.customer_id, a.address_type, a.reference_id, a.company_name, a.address_line_1, " +
            "             a.address_line_2, a.town, a.postcode, a.country, a.state, a.contact_name, a.contact_no, " +
            "             a.contact_email, a.notes, a.is_default, a.count_used, a.is_deleted, a.deleted_at, a.created_at, " +
            "             a.updated_at, a.deleted_by, a.created_by, a.updated_by " +
            "FROM address a " +
            "         LEFT JOIN user cli ON a.client_id = cli.id " +
            "         LEFT JOIN user cus ON a.customer_id = cus.id " +
            "WHERE a.is_deleted = FALSE " +
            "  AND CASE " +
            "          WHEN ?1 = 'ANY' THEN a.address_type IN ('DELIVERY', 'SENDER') " +
            "          ELSE a.address_type = ?1 " +
            "    END " +
            "  AND CASE " +
            "          WHEN ?2 = 'CUSTOMER' THEN ?3 in (cus.id, cli.id)  " +
            "          WHEN ?2 = 'BROKER'   THEN ?3 in (cli.broker_id, cus.id, cli.id) " +
            "          ELSE cli.id = ?3 " +
            "      END";

    String DELETE_BY_IDS = "UPDATE address SET " +
            "is_deleted = TRUE," +
            "deleted_at = NOW(), " +
            "deleted_by = ?2 " +
            "WHERE id IN ?1 " +
            "AND is_deleted = FALSE";

    String USER_ADDRESSES = "SELECT a.id, a.client_id, a.customer_id, a.address_type, a.reference_id, a.company_name, a.address_line_1,  \n" +
            "         a.address_line_2, a.town, a.postcode, a.country, a.state, a.contact_name, a.contact_no,\n" +
            "         a.contact_email, a.notes, a.is_default, a.count_used, a.is_deleted, a.deleted_at, a.created_at,\n" +
            "         a.updated_at, a.deleted_by, a.created_by, a.updated_by\n" +
            "FROM address a\n" +
            "INNER JOIN user cli ON a.client_id = cli.id\n" +
            "INNER JOIN user cus ON a.customer_id = cus.id\n" +
            "WHERE a.is_deleted = FALSE\n" +
            "AND CASE WHEN ?1 = 'CLIENT' THEN ?2 IN (cus.id, cli.id)\n" +
            "         WHEN ?1 = 'CUSTOMER' THEN ?2 IN (cli.broker_id, cus.id, cli.id)\n" +
            "         ELSE cli.id = ?2 END\n" +
            "AND a.id IN ?3";

    @Query(value = ADDRESS_BY_IDS, nativeQuery = true)
    List<Address> findAddresses(List<Long> ids);

    // Get all addresses under the user ID (client or customer)
    @Query(value = ADDRESS_BY_USERID, nativeQuery = true)
    List<Address> findAddresses(String addressType, String userRole, long userId, Pageable pageable);

    @Modifying
    @Query(value = DELETE_BY_IDS, nativeQuery = true)
    Integer deleteAddressesByIds(List<Long> addressIds, long userId);

    @Query(value = ADDRESS_LINE1_LIKE, nativeQuery = true)
    List<Address> findAddresses(String addressType, String userRole, long userId, String criteria);

    @Query(value = USER_ADDRESSES, nativeQuery = true)
    List<Address> findAddresses(String userRole, long userId, List<Long> addressIds);
}
