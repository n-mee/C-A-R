package ui;

import org.junit.jupiter.api.Test;
import repository.ScheduleRepository;
import service.ScheduleService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleUITest {

    private ScheduleService createService() {
        return new ScheduleService(new ScheduleRepository());
    }

    @Test
    void shouldDisplayScheduleForSelectedRoom() {
        String output = runUi("2\nMONDAY\n4-4\n\n0\n");

        assertTrue(output.contains("DAY: MONDAY"));
        assertTrue(output.contains("ROOM: 4-4"));
        assertTrue(output.contains("VIEW FULL-DAY ROOM SCHEDULE"));
        assertTrue(output.contains("Subject   : NSTP 1"));
        assertTrue(output.contains("Professor : Mr. Pahitong"));
    }

    @Test
    void shouldDisplayOccupiedStatusAndAlternatives() {
        String output = runUi("1\nMONDAY\n4-4\n8:00 AM\n9:00 AM\n\n0\n");

        assertTrue(output.contains("STATUS: OCCUPIED"));
        assertTrue(output.contains("Existing schedule(s) causing the conflict:"));
        assertTrue(output.contains("AVAILABLE ALTERNATIVE ROOM(S):"));
        assertTrue(output.contains("4-1 (4th Floor College Building)"));
        assertTrue(output.contains("Press Enter to continue..."));
    }

    @Test
    void shouldRecoverFromInvalidDayInput() {
        String output = runUi("2\nSUNDAY\nMONDAY\n4-4\n\n0\n");

        assertTrue(output.contains("Invalid day. Enter 1-6 or Monday-Saturday."));
        assertTrue(output.contains("DAY: MONDAY"));
    }

    @Test
    void shouldSearchSchedulesBySubject() {
        String output = runUi("3\n1\nNSTP 1\n\n0\n");

        assertTrue(output.contains("SEARCH SCHEDULES"));
        assertTrue(output.contains("Matching schedule(s):"));
        assertTrue(output.contains("DAY: MONDAY"));
        assertTrue(output.contains("Subject   : NSTP 1"));
    }

    @Test
    void shouldDisplayAllRoomSchedulesForSelectedDay() {
        String output = runUi("4\nMONDAY\n\n0\n");

        assertTrue(output.contains("VIEW ALL ROOM SCHEDULES"));
        assertTrue(output.contains("Schedules for all rooms on MONDAY:"));
        assertTrue(output.contains("ROOM: 4-4"));
        assertTrue(output.contains("LOCATION:"));
    }

    @Test
    void shouldExitCleanlyWhenInputCloses() {
        String output = runUi("");

        assertTrue(output.contains("Input closed. Exiting..."));
    }

    private String runUi(String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            new ConsoleUI(createService(), new Scanner(input)).start();
            return output.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(originalOutput);
        }
    }
}
