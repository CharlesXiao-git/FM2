package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Carrier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarrierRepository extends CrudRepository<Carrier, Long> {

    String SELECT_ALL_CARRIERS = "SELECT c.id, c.name, c.display_name, c.is_data_transfer_required, c.data_transfer_frequency,\n" +
            "c.label_type, c.has_suburb_surcharge, c.has_sender_id, c.service_types,\n" +
            "c.pricing, c.metro_zones, c.is_internal_invoicing, c.is_tracking_difot, c.tracking_url, c.is_deleted, c.deleted_at,\n" +
            "c.created_at, c.updated_at, c.deleted_by, c.created_by, c.updated_by \n" +
            "FROM carrier c WHERE c.is_deleted = 0";

    String CARRIERS_BY_IDS = "SELECT c.id, c.name, c.display_name, c.is_data_transfer_required, c.data_transfer_frequency,\n" +
            "c.label_type, c.has_suburb_surcharge, c.has_sender_id, c.service_types,\n" +
            "c.pricing, c.metro_zones, c.is_internal_invoicing, c.is_tracking_difot, c.tracking_url, c.is_deleted, c.deleted_at,\n" +
            "c.created_at, c.updated_at, c.deleted_by, c.created_by, c.updated_by \n" +
            "FROM carrier c \n" +
            "WHERE c.is_deleted = 0\n" +
            "AND c.id IN ?1";

    @Query(value = SELECT_ALL_CARRIERS, nativeQuery = true)
    List<Carrier> getCarriers();

    @Query(value = CARRIERS_BY_IDS, nativeQuery = true)
    List<Carrier> getCarriers(List<Long> ids);
}
