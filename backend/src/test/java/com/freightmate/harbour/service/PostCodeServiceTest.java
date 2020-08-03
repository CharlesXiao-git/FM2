package com.freightmate.harbour.service;

import com.freightmate.harbour.model.AuspostLocalityResponse;
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
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        PostCodeService _service = new PostCodeService(restTemplateBuilder.build(),FAKE_KEY);

        AuspostLocalityResponse localityResponse = _service.getLocality("3018");

        assert !localityResponse.getLocalitiesWrapper().getLocalities().isEmpty();
        LOG.info("Successfully loaded response: {}", localityResponse);
    }

}
