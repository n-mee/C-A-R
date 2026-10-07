package repository;

import model.Schedule;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScheduleRepository {
    private static final String DEFAULT_RESOURCE_PATH = "/schedules.csv";
    private static final DateTimeFormatter CSV_TIME =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final Set<String> VALID_DAYS = Set.of(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY");
    private final String resourcePath;
    private List<Schedule> schedules;

    public ScheduleRepository() {
        this(DEFAULT_RESOURCE_PATH);
    }

    ScheduleRepository(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public List<Schedule> findAll() {
        if (schedules == null) {
            schedules = loadSchedules();
        }
        return schedules;
    }

    private List<Schedule> loadSchedules() {
        InputStream resource = ScheduleRepository.class.getResourceAsStream(resourcePath);
        if (resource == null) {
            throw new IllegalStateException("Schedule resource not found: " + resourcePath);
        }

        try (Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {
            List<Schedule> loadedSchedules = new ArrayList<>();

            for (CSVRecord record : parser) {
                if (record.size() != 7) {
                    throw new IllegalStateException(
                            "Invalid schedule row " + record.getRecordNumber()
                                    + ": expected 7 fields but found " + record.size());
                }

                validateRecord(record);
                loadedSchedules.add(new Schedule(
                        record.get(0),
                        record.get(1),
                        parseTime(record.get(2)),
                        parseTime(record.get(3)),
                        record.get(4),
                        record.get(5),
                        record.get(6)
                ));
            }

            return List.copyOf(loadedSchedules);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read " + resourcePath, exception);
        }
    }

    private static void validateRecord(CSVRecord record) {
        for (int index = 0; index < record.size(); index++) {
            if (record.get(index).isBlank()) {
                throw new IllegalStateException(
                        "Invalid schedule row " + record.getRecordNumber()
                                + ": field " + (index + 1) + " is blank");
            }
        }

        String day = record.get(0).toUpperCase();
        if (!VALID_DAYS.contains(day)) {
            throw new IllegalStateException(
                    "Invalid schedule row " + record.getRecordNumber()
                            + ": unknown day " + record.get(0));
        }

        LocalTime start = parseTime(record.get(2));
        LocalTime end = parseTime(record.get(3));
        if (!start.isBefore(end)) {
            throw new IllegalStateException(
                    "Invalid schedule row " + record.getRecordNumber()
                            + ": start time must be before end time");
        }
    }

    private static LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, CSV_TIME);
        } catch (DateTimeParseException exception) {
            throw new IllegalStateException("Invalid time: " + time, exception);
        }
    }
}
