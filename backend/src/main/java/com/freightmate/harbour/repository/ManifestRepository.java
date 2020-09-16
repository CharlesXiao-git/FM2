package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Manifest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ManifestRepository extends PagingAndSortingRepository<Manifest, Long> {
    // TODO: Need to update the query again once the manifest table is updated to include the user ID
    String MANIFESTS_BY_USER = "SELECT m.id, m.user_client_id, m.goods_pickup_status, m.manifest_number, m.ready_time, m.closing_time, m.data_status,\n" +
            "       m.created_at, m.created_by, m.updated_at, m.updated_by, m.is_deleted, m.deleted_at, m.deleted_by\n" +
            "FROM manifest m\n" +
            "         LEFT JOIN user_client cli ON m.user_client_id = cli.id\n" +
            "         LEFT JOIN user_customer cus ON cli.user_customer_id = cus.id\n" +
            "         LEFT JOIN user_broker bro ON cus.user_broker_id = bro.id\n" +
            "WHERE CASE \n" +
            "          WHEN ?1 = 'BROKER' THEN ?2 = bro.user_id\n" + // check if I'm the broker for it
            "          WHEN ?1 = 'CUSTOMER' THEN ?2 = cus.user_id\n" + // cases when the row is the customer's, or when the customer is the parent of the owner
            "          ELSE ?2 = cli.user_id\n" + // otherwise check if its mine
            "      END \n" +
            "  AND m.is_deleted = FALSE \n";

    String MANIFESTS_BY_IDS = "SELECT m.id, m.user_client_id, m.goods_pickup_status, m.manifest_number, m.ready_time, m.closing_time, m.data_status,\n" +
            "       m.created_at, m.created_by, m.updated_at, m.updated_by, m.is_deleted, m.deleted_at, m.deleted_by\n" +
            "FROM manifest m\n" +
            "WHERE m.id IN ?1\n" +
            "AND m.is_deleted = FALSE";

    @Query(value = MANIFESTS_BY_USER, nativeQuery = true)
    List<Manifest> findManifests(String userRole, long userId, Pageable pageable);

    @Query(value = MANIFESTS_BY_IDS, nativeQuery = true)
    List<Manifest> findManifests(List<Long> ids);
}
