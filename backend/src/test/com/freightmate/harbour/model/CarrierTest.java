package com.freightmate.harbour.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightmate.harbour.repository.ConsignmentRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CarrierTest {

    ObjectMapper mapper;
    ConsignmentRepository consignmentRepository;

    @Test
    public void getCarrierTest() {
        List<String> node = List.of("fpp","exp","prm");
        Carrier build = Carrier.builder()
                .serviceTypes(node)
                .build();
    }

    @Before
    public void setup() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    }
}
