package com.freightmate.harbour.service;

import com.freightmate.harbour.model.ItemType;
import com.freightmate.harbour.repository.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemTypeService {
    private final ItemTypeRepository itemTypeRepository;

    ItemTypeService(@Autowired ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    public List<ItemType> getItemTypes() {
        return itemTypeRepository.getItemTypes();
    }

    public List<ItemType> getItemTypes(List<Long> ids) {
        return itemTypeRepository.getItemTypes(ids);
    }
}
