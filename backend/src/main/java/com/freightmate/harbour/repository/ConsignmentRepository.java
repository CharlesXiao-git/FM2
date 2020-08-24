package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Consignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConsignmentRepository extends PagingAndSortingRepository<Consignment, Long> {
    String CONSIGNMENTS_BY_USER = "SELECT c.id, c.owner_id, c.sender_address_id, c.delivery_address_id, c.connote_id, c.dispatch_date_at, c.delivery_window_start_at,  \n" +
            "                   c.delivery_window_end_at, c.address_class, c.is_allowed_to_leave, c.is_tailgate_required, c.is_deleted, c.deleted_at,  \n" +
            "                   c.created_at, c.updated_at, c.deleted_by, c.created_by, c.updated_by\n" +
            "FROM consignment c\n" +
            "         INNER JOIN user cli ON c.owner_id = cli.id\n" +
            "WHERE cli.user_role = 'CLIENT'\n" +
            "  AND CASE\n" +
            "          WHEN ?1 = 'CLIENT' THEN ?2 in (cli.customer_id, cli.id)\n" +
            "          WHEN ?1 = 'CUSTOMER' THEN ?2 in (cli.broker_id, cli.customer_id, cli.id)\n" +
            "          ELSE cli.id = ?2 END\n" +
            "  AND c.is_deleted = FALSE";

    String CONSIGNMENTS_BY_IDS = "SELECT c.id, c.owner_id, c.sender_address_id, c.delivery_address_id, c.connote_id, c.dispatch_date_at, c.delivery_window_start_at, " +
            "       c.delivery_window_end_at, c.address_class, c.is_allowed_to_leave, c.is_tailgate_required, c.is_deleted, c.deleted_at, " +
            "       c.created_at, c.updated_at, c.deleted_by, c.created_by, c.updated_by " +
            "FROM consignment c " +
            "WHERE c.id IN ?1 " +
            "AND c.is_deleted = FALSE";

    String DELETE_BY_IDS = "UPDATE consignment c SET " +
            "c.is_deleted = TRUE," +
            "c.deleted_at = NOW(), " +
            "c.deleted_by = ?2 " +
            "WHERE c.id IN ?1 " +
            "AND c.owner_id = ?2";

    @Query(value = CONSIGNMENTS_BY_USER, nativeQuery = true)
    List<Consignment> findConsignments(String userRole, long userId, Pageable pageable);

    @Query(value = CONSIGNMENTS_BY_IDS, nativeQuery = true)
    List<Consignment> findConsignments(List<Long> ids);

    @Query(value = DELETE_BY_IDS, nativeQuery = true)
    @Modifying
    void deleteConsignments(List<Long> ids, long userId);
}
