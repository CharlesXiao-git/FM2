package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.ItemType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemTypeRepository extends CrudRepository<ItemType, Long> {
    String ALL_ITEM_TYPES = "SELECT i.id, i.type, " +
            "i.is_mutable, i.is_deleted, i.deleted_at, i.created_at, i.updated_at, i.deleted_by, " +
            "i.created_by, i.updated_by " +
            "FROM item_type i " +
            "WHERE NOT i.is_deleted";

    String FIND_ITEM_TYPES_BY_IDS = "SELECT i.id, i.type, " +
            "i.is_mutable, i.is_deleted, i.deleted_at, i.created_at, i.updated_at, i.deleted_by, " +
            "i.created_by, i.updated_by " +
            "FROM item_type i " +
            "WHERE i.id IN ?1 " +
            "AND NOT i.is_deleted";

    @Query(value = ALL_ITEM_TYPES, nativeQuery = true)
    List<ItemType> getItemTypes();

    @Query(value = FIND_ITEM_TYPES_BY_IDS, nativeQuery = true)
    List<ItemType> getItemTypes(List<Long> ids);
}
