package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.Suburb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface SuburbRepository extends PagingAndSortingRepository<Suburb, Long>  {

    String SUBURB_LOOKUP = "SELECT s.id, s.name, s.postcode, s.state, s.country, s.created_at, s.created_by, s.updated_at, s.updated_by,\n" +
            "       s.is_deleted, s.deleted_at, s.deleted_by\n" +
            "FROM suburb s\n" +
            "WHERE s.name = ?1\n" +
            "AND s.postcode = ?2";

    @Query(value = SUBURB_LOOKUP, nativeQuery = true)
    List<Suburb> findSuburbs(String name, long postcode);

    List<Suburb> findAll();

}
