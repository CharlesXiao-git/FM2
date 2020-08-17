package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.ItemType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemTypeRepository extends CrudRepository<ItemType, Long> {
    String ALL_ITEM_TYPES = "SELECT i.id, i.name, i.quantity, i.length, i.width, i.height, i.weight, " +
            "i.is_mutable, i.is_custom, i.is_deleted, i.deleted_at, i.created_at, i.updated_at, i.deleted_by, " +
            "i.created_by, i.updated_by " +
            "FROM item_type i " +
            "WHERE i.is_custom = ?1";

    @Query(value = ALL_ITEM_TYPES, nativeQuery = true)
    List<ItemType> getItemTypes(Boolean isCustom);
}
