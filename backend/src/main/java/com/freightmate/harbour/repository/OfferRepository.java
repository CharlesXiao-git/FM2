package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OfferRepository extends CrudRepository<Offer, Long> {
    String GET_OFFERS = "SELECT o.id, o.quote_id, o.consignment_id, o.carrier_account_id, o.ETA, o.freight_cost, " +
            "o.category_1_fees, o.category_2_fees, o.fuel_surcharge, o.gst, o.total_cost, o.selected, " +
            "o.is_deleted, o.deleted_at, o.created_at, o.updated_at, o.deleted_by, o.created_by, o.updated_by " +
            "FROM offer o" ;

    @Query(value = GET_OFFERS, nativeQuery = true)
    List<Offer> getOffers();
}
