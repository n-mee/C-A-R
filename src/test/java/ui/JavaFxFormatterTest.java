package ui;

import model.AvailabilityResult;
import model.Room;
import model.Schedule;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaFxFormatterTest {
    @Test
    void shouldFormatAvailableResult() {
        String output = JavaFxFormatter.availability(
                new AvailabilityResult(true, List.of(), List.of()),
                "MONDAY", "201", LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertTrue(output.contains("MONDAY, room 201, 8:00 AM - 9:00 AM is AVAILABLE."));
        assertTrue(output.contains("No scheduled class or activity overlaps this time."));
    }

    @Test
    void shouldFormatOccupiedResultWithAlternatives() {
        Schedule conflict = schedule("MONDAY", "4-4", "NSTP 1");
        String output = JavaFxFormatter.availability(
                new AvailabilityResult(
                        false, List.of(conflict),
                        List.of(new Room("4-1", "4th Floor College Building"))),
                "MONDAY", "4-4", LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertTrue(output.contains("is OCCUPIED."));
        assertTrue(output.contains("- NSTP 1 (CRIM 1B)"));
        assertTrue(output.contains("Alternative rooms: 4-1"));
    }

    @Test
    void shouldFormatSearchResultsWithDayAndLocation() {
        String output = JavaFxFormatter.searchResults(
                List.of(schedule("SATURDAY", "201", "NSTP 2")),
                Map.of("201", "High School Building"));

        assertTrue(output.contains("DAY: SATURDAY"));
        assertTrue(output.contains("ROOM: 201 (High School Building)"));
        assertTrue(output.contains("Subject: NSTP 2"));
    }

    @Test
    void shouldFormatAllRoomSchedules() {
        String output = JavaFxFormatter.allRoomSchedules(
                "SATURDAY",
                Map.of("201", List.of(schedule("SATURDAY", "201", "NSTP 2"))),
                Map.of("201", "High School Building"));

        assertTrue(output.contains("Schedules for all rooms on SATURDAY:"));
        assertTrue(output.contains("ROOM: 201 (High School Building)"));
        assertTrue(output.contains("Professor: Professor"));
    }

    private Schedule schedule(String day, String room, String subject) {
        return new Schedule(
                day, room, LocalTime.of(8, 0), LocalTime.of(9, 0),
                subject, "CRIM 1B", "Professor");
    }
}
