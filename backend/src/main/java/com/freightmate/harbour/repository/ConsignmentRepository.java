package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Consignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConsignmentRepository extends PagingAndSortingRepository<Consignment, Long> {
    String CONSIGNMENTS_BY_USER = "SELECT c.id, c.client_id, c.sender_address_id, c.delivery_address_id, c.connote_id, c.dispatch_date_at, c.delivery_window_start_at, " +
            "       c.delivery_window_end_at, c.address_class, c.is_allowed_to_leave, c.is_tailgate_required, c.is_deleted, c.deleted_at, " +
            "       c.created_at, c.updated_at, c.deleted_by, c.created_by, c.updated_by, c.original_id " +
            "FROM consignment c " +
            "WHERE c.client_id = ?1 " +
            "AND c.is_deleted = FALSE";

    String CONSIGNMENTS_BY_IDS = "SELECT c.id, c.client_id, c.sender_address_id, c.delivery_address_id, c.connote_id, c.dispatch_date_at, c.delivery_window_start_at, " +
            "       c.delivery_window_end_at, c.address_class, c.is_allowed_to_leave, c.is_tailgate_required, c.is_deleted, c.deleted_at, " +
            "       c.created_at, c.updated_at, c.deleted_by, c.created_by, c.updated_by, c.original_id " +
            "FROM consignment c " +
            "WHERE c.id IN ?1 " +
            "AND c.is_deleted = FALSE";

    String DELETE_BY_IDS = "UPDATE consignment c SET " +
            "c.is_deleted = TRUE," +
            "c.deleted_at = NOW() " +
            "WHERE c.id IN ?1 " +
            "AND c.client_id = ?2";

    @Query(value = CONSIGNMENTS_BY_USER, nativeQuery = true)
    List<Consignment> findConsignments(long userId, Pageable pageable);

    @Query(value = CONSIGNMENTS_BY_IDS, nativeQuery = true)
    List<Consignment> findConsignments(List<Long> ids);

    @Query(value = DELETE_BY_IDS, nativeQuery = true)
    @Modifying
    void deleteConsignments(List<Long> ids, long userId);
}
