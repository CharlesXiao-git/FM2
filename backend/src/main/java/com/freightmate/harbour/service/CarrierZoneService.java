package com.freightmate.harbour.service;

import com.freightmate.harbour.helper.ListHelper;
import com.freightmate.harbour.model.CarrierAccount;
import com.freightmate.harbour.model.CarrierZone;
import com.freightmate.harbour.model.CarrierZoneRaw;
import com.freightmate.harbour.model.Suburb;
import com.freightmate.harbour.repository.CarrierAccountRepository;
import com.freightmate.harbour.repository.SuburbRepository;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarrierZoneService {

    private static final Logger LOG = LoggerFactory.getLogger(CarrierZoneService.class);
    @PersistenceContext
    private final EntityManager entityManager;
    private final int INSERT_BATCH_SIZE;
    private final CarrierAccountRepository carrierAccountRepo;
    private final SuburbRepository suburbRepo;
    private static final String UPSERT_ZONE_QUERY = "INSERT INTO carrier_zone " +
            "(suburb_id, carrier_account_id, zone, created_by) " +
            "VALUES %s " +
            "ON DUPLICATE KEY UPDATE " +
            "zone         = VALUES(zone), " +
            "updated_by   = VALUES(created_by), " +
            "updated_at   = NOW()";

    CarrierZoneService(
            @Autowired EntityManagerService ems,
            @Autowired CarrierAccountRepository carrierAccountRepo,
            @Value("${spring.datasource.default-batch-size}") String insertBatchSize,
            @Autowired SuburbRepository suburbRepo) {
        this.entityManager = ems.getEntityManager();
        this.carrierAccountRepo = carrierAccountRepo;
        this.suburbRepo = suburbRepo;
        this.INSERT_BATCH_SIZE = Integer.parseInt(insertBatchSize);
    }

    /**
     * Convert a row from a zones CSV into a CarrierZoneRaw (postcode, suburbTown, suburb, carrierName, zoneCode).
     * This record contains strings for postcode, suburb, suburb town, zoneCode and carrier
     * We create a record for each carrier-suburb pair
     * These records are then flat mapped into a large list across all suburbs/postcodes.
     * <p>
     * Example:
     * postcode, suburb, suburbTown, tnt, startrack1, startrack2
     * "3000, Melbourne, 3000  --  Melbourne, MEL, VIC1, MELB
     * <p>
     * Becomes:
     * [{"postcode","3000", "name":"Melbourne","carrierName":"tnt","zoneCode":"MEL"},
     * {"postcode","3000", "name":"Melbourne","carrierName":"startrack1","zoneCode":"VIC1"},
     * {"postcode","3000", "name":"Melbourne","carrierName":"startrack2","zoneCode":"MELB"}]
     *
     * @param records the CSV rows as List<CSVRecord>
     * @return List<CarrierZoneRaw> the parsed CSV records
     */
    public List<CarrierZoneRaw> parseCarrierZones(List<CSVRecord> records) {
        //todo explain
        List<String> headers = ListHelper.getListFromIterator(records.get(0).iterator()).subList(0, records.get(0).size() - 1);

        return records.subList(1, records.size())
                .parallelStream()
                .flatMap(record -> {
                    String postcode = record.get(0);
                    String suburbTown = record.get(1).replace("\"", "").replace("'", "").replace("O\\'", "");

                    List<CarrierZoneRaw> zones = new ArrayList<>();
                    Iterator<String> iterator = record.iterator();

                    for (int index = 0; iterator.hasNext(); index++) {
                        if (index < 3 || index == record.size() - 1) {
                            iterator.next();
                            continue;
                        }

                        String val = iterator.next().replace("\"", "");

                        if (val.toLowerCase().contains("n/a") || val.equalsIgnoreCase("void")) {
                            continue;
                        }

                        zones.add(
                                CarrierZoneRaw
                                        .builder()
                                        .postcode(postcode)
                                        .suburbTown(suburbTown)
                                        .carrierName(headers.get(index))
                                        .zone(val)
                                        .build()
                        );
                    }

                    return zones.stream();
                })
                .collect(Collectors.toList());
    }

    /**
     * Generate the values string from a List<RawCarrierZone>
     * This string is in the format shown below.
     *
     * (suburbid1, carrierAccountId1, zoneCode1, userId),
     * (suburbid2, carrierAccountId2, zoneCode2, userId).
     * ...
     *
     * @param rawZones a List of CarrierZones with string names instead of ids for carrier and suburb
     * @param suburbs The suburbs to search for the ID in
     * @param carriers the carrier relationships to look for the carrier in
     * @return string to be added into the sql insert statement
     */
    public String prepareUpsertSqlValues(List<CarrierZoneRaw> rawZones,
                                         Map<String, Suburb> suburbs,
                                         Map<String, CarrierAccount> carriers,
                                         long userId) {

        return rawZones
                .stream()
                .map(rawZone -> {
                    // need suburb name and postcode for a unique key
                    Suburb suburb = suburbs.get(rawZone.getSuburbTown().toLowerCase().concat(rawZone.getPostcode()));
                    if (Objects.isNull(suburb)) {
                        LOG.error("suburb for zone not found: {}", rawZone);
                    }

                    CarrierAccount carrierAccount = carriers.get(rawZone.getCarrierName());

                    if (Objects.isNull(carrierAccount)) {
                        LOG.error("carrier for zone not found: {}", rawZone);
                    }
                    // Use the Carriers and Suburbs list from above to convert the raw zones to CarrierZone models
                    return CarrierZone
                            .builder()
                            .carrierAccountId(carrierAccount.getId())
                            .suburbId(suburb.getId())
                            .zone(rawZone.getZone())
                            .build()
                            .toSqlValueString(userId);

                }).collect(Collectors.joining(","));

    }

    /**
     * More often than not (for now) we will be dealing with Tuco as the only broker.
     * When we introduce more we will need to pass the broker ID in the request payload
     *
     * @param rawZones a List of CarrierZones with string names instead of ids for carrier and suburb
     * @return Integer count of rows inserted or updated.
     */
    @Transactional
    public Integer batchUpsertZones(List<CarrierZoneRaw> rawZones, long userId) {
        return batchUpsertZones(rawZones, 1, userId); // default broker to tuco as they are the only one currently
    }


    /**
     * Receives a list<CarrierZoneRaw> and upsert them into the DB.
     * This process is implemented by executing native SQL, and building a query in the format
     *
     *      INSERT INTO carrier_zone (suburb_id, carrier_account_id, zone, created_by)
     *      VALUES
     *           (1,20,'MEL',533),
     *           (2,21,'MEL-MET',533),
     *           (3,22,'1',533),
     *           (4,23,'VIC1',533),
     *           (5,24,'AUS',533)
     *      ON DUPLICATE KEY UPDATE
     *      zone         = VALUES(zone),
     *      updated_by   = VALUES(created_by),
     *      updated_at   = NOW();
     *
     * This will attempt to insert new rows, but if it finds a row with amatching carrier_account_id, and suburb_id
     * then it will just update the values of the `zone`, `updated_by` and `updated_at` fields
     *
     * @param rawZones the raw carrier zones aprsed from a CSV
     * @param brokerId the broker these zones apply to
     * @param userId the user executing this operation
     * @return the number of updated/inserted rows
     */
    @Transactional
    public Integer batchUpsertZones(List<CarrierZoneRaw> rawZones, long brokerId, long userId) {
        // get all suburbs and create a map with the key being the suburb name, and the value being the suburb object
        Map<String, Suburb> suburbs = suburbRepo
                .findAll()
                .parallelStream()
                .collect(Collectors.toMap(Suburb::getSuburbKey, suburb -> suburb, (s1, s2) -> s1));

        // get all CarrierAccounts and create a map with the key being the carrier name, and the value being the carrier object
        Map<String, CarrierAccount> carriers = carrierAccountRepo
                .findAllByUserBrokerId(brokerId)
                .stream()
                .collect(Collectors.toMap(
                        carrierAccount -> carrierAccount.getCarrier().getName(),
                        carrierAccount -> carrierAccount)
                );


        return ListHelper.batch(rawZones, INSERT_BATCH_SIZE * 10) // split the suburbs into batches, as the rows are small make the batches bigger
                .map(listSubset ->
                        // prepare each batches sql values strings
                        this.prepareUpsertSqlValues(
                                listSubset,
                                suburbs,
                                carriers,
                                userId
                        )
                )
                .map(upsertSqlValues ->
                        // execute each batch upsert
                        this.entityManager
                                .createNativeQuery(
                                        String.format(
                                                UPSERT_ZONE_QUERY,
                                                upsertSqlValues
                                        )
                                ).executeUpdate()
                )
                .mapToInt(count -> count) // convert int -> Integer to be summed for total record count updated
                .sum();
    }
}
