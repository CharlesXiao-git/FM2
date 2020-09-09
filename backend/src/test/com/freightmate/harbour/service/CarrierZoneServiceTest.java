package com.freightmate.harbour.service;

import com.freightmate.harbour.model.CarrierZoneRaw;
import com.freightmate.harbour.repository.CarrierAccountRepository;
import com.freightmate.harbour.repository.SuburbRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class CarrierZoneServiceTest {

    // These are not needed for the current tests, just putting mocks in place.
    EntityManagerService dms = Mockito.mock(EntityManagerService.class);
    CarrierAccountRepository car = Mockito.mock(CarrierAccountRepository.class);
    SuburbRepository sr = Mockito.mock(SuburbRepository.class);

    private final ZoneService zoneService = new ZoneService(dms,car,sr);

    @Test
    public void ShouldParseRecordsIntoRawZone_WhenProvidedCSVRecords() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("zonesSmall.csv");
        assert resourceAsStream != null;

        List<CSVRecord> records = CSVFormat
                .newFormat(',')
                .parse(
                        new InputStreamReader(resourceAsStream)
                ).getRecords();

        List<CarrierZoneRaw> rawCarrierZones = zoneService.parseCarrierZones(records);
        assert rawCarrierZones.size() == 106;
    }

}
