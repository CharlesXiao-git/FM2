package com.freightmate.harbour.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightmate.harbour.model.AuspostLocalityResponse;
import com.freightmate.harbour.model.AuspostLocalityWrapper;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class PostCodeServiceTest {

    private static final String FAKE_KEY = "FAKE KEY HERE";
    private static final Logger LOG = LoggerFactory.getLogger(PostCodeServiceTest.class);

    // This test will actually call the endpoint run to play with API if required.
    // It is ignored so it won't be run as part of the test suite. It may be useful later though.
    @Test
    @Ignore
    public void ShouldCallAuspost_WhenProvidedCorrectApiKey(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);


        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        PostCodeService _service = new PostCodeService(
                restTemplateBuilder.build(),
                FAKE_KEY,
                mapper
        );

        AuspostLocalityWrapper localityWrapper = _service.getLocality("3000");

        assert !localityWrapper.getLocalities().isEmpty();
        LOG.info("Successfully loaded response: {}", localityWrapper);
    }

}
