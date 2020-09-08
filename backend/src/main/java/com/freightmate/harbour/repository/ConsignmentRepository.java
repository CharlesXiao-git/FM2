package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Consignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConsignmentRepository extends PagingAndSortingRepository<Consignment, Long> {
    String CONSIGNMENTS_BY_USER = "SELECT c.id, c.user_client_id, c.quote_id, c.manifest_id, c.sender_address_id, c.delivery_address_id,\n" +
            "       c.connote_number, c.who_pays, c.payment_account_number, c.authority_to_leave, c.is_tailgate_required,\n" +
            "        c.delivery_address_class, c.dispatched_at, c.delivery_window_begin, c.delivery_window_end, c.special_instructions,\n" +
            "       c.scale_from, c.scale_to, c.base_charge, c.rate_charge, c.min_charge, c.fuel, c.cubic_conversion_rate,  c.consignment_type,\n" +
            "       c.residential_address_charge, c.tailgate_charge, c.dg_charge, c.oversize_charge,  c.status, \n" +
            "       c.status_booked, c.status_in_transit, c.status_out_for_delivery, c.status_delivered, c.status_unable_to_deliver, \n" +
            "       c.status_note, c.status_late, c.status_latest_timestamp, c.total_item_weight, c.total_item_cubic, c.total_item_qty, \n" +
            "       c.freight_cost, c.cat_1_fee, c.fuel_levy, c.cat_2_fee, c.gst, c.total_cost, c.poa, c.service_title_1, c.service_title_2, \n" +
            "       c.service_title_3, c.created_at, c.created_by, c.updated_at, c.updated_by, c.is_deleted, c.deleted_at, c.deleted_by \n" +
            "FROM consignment c\n" +
            "         LEFT JOIN user_client cli ON c.user_client_id = cli.id\n" +
            "         LEFT JOIN user_customer cus ON cli.user_customer_id = cus.id\n" +
            "         LEFT JOIN user_broker bro ON cus.user_broker_id = bro.id\n" +
            "WHERE CASE \n" +
            "          WHEN ?1 = 'BROKER' THEN ?2 = bro.user_id\n" + // check if I'm the broker for it
            "          WHEN ?1 = 'CUSTOMER' THEN ?2 = cus.user_id\n" + // cases when the row is the customer's, or when the customer is the parent of the owner
            "          ELSE ?2 = cli.user_id\n" + // otherwise check if its mine
            "      END \n" +
            "  AND c.is_deleted = FALSE \n";

    String CONSIGNMENTS_BY_IDS = "SELECT c.id, c.user_client_id, c.quote_id, c.manifest_id, c.sender_address_id, c.delivery_address_id,\n" +
            "       c.connote_number, c.who_pays, c.payment_account_number, c.authority_to_leave, c.is_tailgate_required,\n" +
            "        c.delivery_address_class, c.dispatched_at, c.delivery_window_begin, c.delivery_window_end, c.special_instructions,\n" +
            "       c.scale_from, c.scale_to, c.base_charge, c.rate_charge, c.min_charge, c.fuel, c.cubic_conversion_rate,  c.consignment_type,\n" +
            "       c.residential_address_charge, c.tailgate_charge, c.dg_charge, c.oversize_charge,  c.status, \n" +
            "       c.status_booked, c.status_in_transit, c.status_out_for_delivery, c.status_delivered, c.status_unable_to_deliver, \n" +
            "       c.status_note, c.status_late, c.status_latest_timestamp, c.total_item_weight, c.total_item_cubic, c.total_item_qty, \n" +
            "       c.freight_cost, c.cat_1_fee, c.fuel_levy, c.cat_2_fee, c.gst, c.total_cost, c.poa, c.service_title_1, c.service_title_2, \n" +
            "       c.service_title_3, c.created_at, c.created_by, c.updated_at, c.updated_by, c.is_deleted, c.deleted_at, c.deleted_by \n" +
            "FROM consignment c " +
            "WHERE c.id IN ?1 " +
            "AND c.is_deleted = FALSE";

    String DELETE_BY_IDS = "UPDATE consignment c SET " +
            "c.is_deleted = TRUE," +
            "c.deleted_at = NOW(), " +
            "c.deleted_by = ?2 " +
            "WHERE c.id IN ?1 ";

    @Query(value = CONSIGNMENTS_BY_USER, nativeQuery = true)
    List<Consignment> findConsignments(String userRole, long userId, Pageable pageable);

    @Query(value = CONSIGNMENTS_BY_IDS, nativeQuery = true)
    List<Consignment> findConsignments(List<Long> ids);

    @Query(value = DELETE_BY_IDS, nativeQuery = true)
    @Modifying
    void deleteConsignments(List<Long> ids, long userId);
}
