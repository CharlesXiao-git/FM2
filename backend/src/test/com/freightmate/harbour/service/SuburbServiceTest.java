package com.freightmate.harbour.service;

import com.freightmate.harbour.model.Suburb;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SuburbServiceTest {

    // These are not needed for the current tests, just putting mocks in place.
    EntityManagerService dms = Mockito.mock(EntityManagerService.class);
    private final SuburbService zoneService = new SuburbService(dms, "500");

    @Test
    public void ShouldParseRecordsIntoRawZone_WhenProvidedSmallCSVRecords() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("zonesSmall.csv");
        assertNotNull(String.format("Unable to find fi;e in test resources: %s","zones.csv"),resourceAsStream);

        List<CSVRecord> records = CSVFormat
                .newFormat(',')
                .parse(
                        new InputStreamReader(resourceAsStream)
                ).getRecords();

        List<Suburb> suburbs = zoneService.parseSuburbs(records);
        assertEquals("Expected number of suburbs does not match", 7,suburbs.size());
    }

    @Test
    public void ShouldParseRecordsIntoRawZone_WhenProvidedCSVRecords() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("zones.csv");
        assertNotNull(String.format("Unable to find fi;e in test resources: %s","zones.csv"),resourceAsStream);

        List<CSVRecord> records = CSVFormat
                .newFormat(',')
                .parse(
                        new InputStreamReader(resourceAsStream)
                ).getRecords();

        List<Suburb> suburbs = zoneService.parseSuburbs(records);
        Map<String, List<Suburb>> suburbMap = suburbs
                .stream()
                .collect(Collectors.groupingBy(suburb -> suburb.getSuburbTown().toLowerCase()));

        assertEquals("Expected number of suburbs does not match", 19983,suburbs.size());
        assertEquals("Expected number of Suburb pojos for 'melbourne' does not match", 4,suburbMap.get("melbourne").size());
    }

}
