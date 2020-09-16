package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.BadRequestException;
import com.freightmate.harbour.model.AuspostLocality;
import com.freightmate.harbour.model.Suburb;
import com.freightmate.harbour.repository.SuburbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.freightmate.harbour.helper.ListHelper;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuburbService {
    @PersistenceContext private final EntityManager entityManager;
    private static final Logger LOG = LoggerFactory.getLogger(SuburbService.class);
    private final int INSERT_BATCH_SIZE;
    private final SuburbRepository suburbRepository;
    private static final String UPSERT_SUBURB_QUERY = "INSERT INTO suburb " +
            "(postcode, name, created_by) " +
            "VALUES %s " +
            "ON DUPLICATE KEY UPDATE postcode=postcode";

    SuburbService(
            @Autowired EntityManagerService ems,
            @Autowired SuburbRepository suburbRepository,
            @Value("${spring.datasource.default-batch-size}") String insertBatchSize ){
        this.entityManager = ems.getEntityManager();
        this.suburbRepository = suburbRepository;
        this.INSERT_BATCH_SIZE = Integer.parseInt(insertBatchSize);
    }

    /**
     * Takes a List<CSVRecord> and converts it into a Suburb Entity.
     * It skips the first row (expecting a header) and then converts each other row, skipping and logging on any failed
     * @param records
     * @return
     */
    public List<Suburb> parseSuburbs(List<CSVRecord> records){
        return records.subList( 1, records.size())
                .parallelStream()
                .map(record -> {
                    try {
                        return Suburb
                                .builder()
                                .postcode(Integer.parseInt(record.get(0)))
                                .name(record.get(1).replace("\"", ""))
                                .build();
                    } catch (Exception e){
                        LOG.error("Unable to parse record: {}", record);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Receives a list<Suburb> and upsert them into the DB.
     * This process is implemented by executing native SQL, and building a query in the format
     *
     *      INSERT INTO suburb (postcode, name, created_by)
     *      VALUES
     *           (3000,'MELBOURNE',533),
     *           (3018,'ALTONA',533),
     *           (3806,'BERWICK',533),
     *           (3124,'CAMBERWELL',533)
     *      ON DUPLICATE KEY UPDATE postcode=postcode ; If theres a duplicate do nothing.
     *
     * This will attempt to insert new rows, but if it finds a row with a matching name, and postcode
     * then it will just skip it and move on
     *
     * @param userId the user executing this operation
     * @return the number of updated/inserted rows
     */

    @Transactional
    public Integer batchUpsertSuburbs(List<Suburb> suburbs, long userId) {

        return ListHelper.batch(suburbs, INSERT_BATCH_SIZE) // split the suburbs into batches of size INSERT_BATCH_SIZE'
                .map(listSubset -> listSubset // create a string in the format (postcode1,suburb1, userId), (postcode2,suburb2, userId) for each row in the batch
                        .stream()
                        .map(suburb -> suburb.toSqlValueString(userId))
                        .collect(Collectors.joining(","))
                ).map(insertSubsetValuesString -> entityManager // upsert each batch
                        .createNativeQuery(String.format(UPSERT_SUBURB_QUERY, insertSubsetValuesString))
                        .executeUpdate()
                ).mapToInt(count -> count) // convert int -> Integer to be summed for total record count updated
                .sum();
    }

    public List<Suburb> getSuburbs(String name, long postcode) {
        return suburbRepository.findSuburbs(name, postcode);
    }

    public void updateSuburb(Suburb suburb) {
        suburbRepository.save(suburb);
    }

    public Suburb populateSuburbFromAuspost(List<AuspostLocality> matchingLocals) {
        // Do a lookup of the suburb in our suburb table
        Optional<Suburb> optSuburb = getSuburbs(matchingLocals.get(0).getLocation(),
                matchingLocals.get(0).getPostcode()
        ).stream().findFirst();
        if (optSuburb.isEmpty()) {
            throw new BadRequestException("Address suburb is not within the Freightmate suburb list");
        }

        Suburb suburb = optSuburb.get();

        // Set the Auspost suburb, postcode and state into the new address to ensure that we have consistent data
        suburb.setName(matchingLocals.get(0).getLocation());
        suburb.setPostcode(matchingLocals.get(0).getPostcode());
        suburb.setState(matchingLocals.get(0).getState());
        return suburb;
    }

    public boolean isInvalidPostcode(String postcode){
        try{
            int code = Integer.parseInt(postcode);
            return code > 9999 || code < 100;
        } catch (NullPointerException|NumberFormatException e){
            return true;
        }
    }
}
