package com.freightmate.harbour.service;

import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AuspostLocality;
import com.freightmate.harbour.model.AuspostLocalityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostCodeService {
    private final RestTemplate restTemplate;
    private final String apiKey;
    private static final String ENDPOINT_URL = "https://digitalapi.auspost.com.au/postcode/search.json";
    private static final Logger LOG = LoggerFactory.getLogger(PostCodeService.class);

    PostCodeService(@Autowired RestTemplate restTemplate,
                    @Value("${auspost.api-key}") String apiKey){
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    /**
     * @param searchVal A postcode or string to search by. Whitespace must be encoded
     * @return string suburb state postcode as defined but auspost
     * @throws HttpClientErrorException on HttpClient error
     */
    public AuspostLocalityResponse getLocality(String searchVal) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("AUTH-KEY", apiKey);

        // Auspost API doesn't like whitespace and first word should be enough to get some results
        if (searchVal.contains(" ")){
            searchVal = searchVal.split(" ")[0];
        }

        UriComponentsBuilder urlBuilder = UriComponentsBuilder
                .fromHttpUrl(ENDPOINT_URL)
                .queryParam("q", searchVal)
                .queryParam("excludePostBoxFlag", true);

        try {
            ResponseEntity<AuspostLocalityResponse> responseEntity = restTemplate.exchange(
                    urlBuilder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    AuspostLocalityResponse.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            //ensure we log this exception then rethrow for the caller to handle
            LOG.error("Unable to lead postcode from Auspost API: ",e);
            throw e;
        }
    }

    public boolean isInvalidPostcode(String postcode){
        try{
            int code = Integer.parseInt(postcode);
            return code > 9999 || code < 100;
        } catch (NullPointerException|NumberFormatException e){
            return true;
        }
    }

    public List<AuspostLocality> getMatchingLocalitiesBySuburb(Address addressRequest) {
        return this
                .getLocality(String.valueOf(addressRequest.getPostcode()))
                .getLocalitiesWrapper()
                .getLocalities()
                .stream()
                .filter(element -> element.getLocation().equalsIgnoreCase(addressRequest.getTown()))
                .collect(Collectors.toList());
    }
}


