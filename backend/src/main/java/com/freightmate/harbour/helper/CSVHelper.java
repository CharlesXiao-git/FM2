package com.freightmate.harbour.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVHelper {

    private CSVHelper() {
        throw new IllegalStateException("Helper class shouldn't be constructed");
    }

    public static List<CSVRecord> recordsFromBytes(byte[] bytes) throws IOException {
        return CSVHelper.recordsFromBytes(bytes, ',');
    }

    public static List<CSVRecord> recordsFromBytes(byte[] bytes, char delimiter) throws IOException {
        return CSVFormat
                .newFormat(delimiter)
                .parse(
                        new InputStreamReader(
                                new ByteArrayInputStream(bytes),
                                StandardCharsets.UTF_8
                        )
                ).getRecords();
    }
}
