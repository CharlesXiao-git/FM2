package com.freightmate.harbour.service;

import com.freightmate.harbour.model.Carrier;
import com.freightmate.harbour.repository.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrierService {
    private final CarrierRepository carrierRepository;

    public CarrierService(@Autowired CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    // Read
    public List<Carrier> getAllCarriers() {
        return carrierRepository
                .findAllCarriers();
    }
}
