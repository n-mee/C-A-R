package ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import model.AvailabilityResult;
import model.Room;
import model.Schedule;
import service.ScheduleService;

public class ConsoleUI {
    private static final String SEPARATOR = "------------------------------------------";
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ROOT);

    private final ScheduleService service;
    private final Scanner scanner;

    public ConsoleUI(ScheduleService service) {
        this(service, new Scanner(System.in));
    }

    ConsoleUI(ScheduleService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            clearScreen();
            showMenu();

            try {
                switch (readLineOrExit().trim()) {
                case "1" -> {
                    checkAvailability();
                    pause();
                }
                case "2" -> {
                    viewSchedule();
                    pause();
                }
                case "3" -> {
                    searchSchedules();
                    pause();
                }
                case "4" -> {
                    viewDaySchedules();
                    pause();
                }
                case "0" -> {
                    System.out.println("\nThank you for using the system!");
                    return;
                }
                default -> {
                    showError("Invalid choice. Please select 1-4 or 0.");
                }
                }
            } catch (EndOfInputException exception) {
                System.out.println("\nInput closed. Exiting...");
                return;
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("       CLASSROOM AVAILABILITY SYSTEM     ");
        System.out.println("==========================================");
        System.out.println("  [1] Check classroom availability");
        System.out.println("  [2] View full-day room schedule");
        System.out.println("  [3] Search schedules");
        System.out.println("  [4] View all room schedules for a day");
        System.out.println();
        System.out.println("  [0] Exit");
        System.out.println(SEPARATOR);
        System.out.print("Enter your choice: ");
    }

    private void checkAvailability() {
        printTitle("CHECK CLASSROOM AVAILABILITY");

        String day = getDay();
        String room = getRoom();
        LocalTime start = getTime("Enter start time (e.g. 7:00 AM): ");
        LocalTime end = getTime("Enter end time (e.g. 12:00 PM): ");

        if (!end.isAfter(start)) {
            showError("Invalid time range. End time must be later than start time.");
            return;
        }

        AvailabilityResult result = service.checkAvailability(day, room, start, end);

        printRoomContext(day, room);
        System.out.println("TIME: " + formatTime(start) + " - " + formatTime(end));
        System.out.println(SEPARATOR);
        System.out.println();

        if (result.available()) {
            System.out.println("STATUS: AVAILABLE");
            System.out.println("There is no scheduled class/activity during this time.");
            return;
        }

        System.out.println("STATUS: OCCUPIED");
        System.out.println();
        System.out.println("Existing schedule(s) causing the conflict:");
        result.conflicts().forEach(this::printSchedule);

        List<Room> availableRooms = result.alternatives();

        if (availableRooms.isEmpty()) {
            System.out.println();
            System.out.println("No other available room was found for this time.");
        } else {
            System.out.println();
            System.out.println("AVAILABLE ALTERNATIVE ROOM(S):");
            availableRooms.forEach(availableRoom ->
                    System.out.println("- " + availableRoom.number()
                            + " (" + availableRoom.location() + ")"));
        }
    }

    private void viewSchedule() {
        printTitle("VIEW FULL-DAY ROOM SCHEDULE");

        String day = getDay();
        String room = getRoom();
        List<Schedule> schedules = service.findSchedule(day, room);

        printRoomContext(day, room);

        if (schedules.isEmpty()) {
            System.out.println("No schedule is listed for this room on " + day + ".");
            return;
        }

        System.out.println("Classes and activities scheduled for the full day:");
        printGroupedSchedules(schedules);
    }

    private void searchSchedules() {
        printTitle("SEARCH SCHEDULES");

        String searchType = getSearchType();
        System.out.print("Enter search text: ");
        String query = readLineOrExit().trim();

        List<Schedule> schedules = switch (searchType) {
            case "1" -> service.searchBySubject(query);
            case "2" -> service.searchBySection(query);
            case "3" -> service.searchByProfessor(query);
            default -> List.of();
        };

        if (schedules.isEmpty()) {
            System.out.println("No schedules matched your search.");
            return;
        }

        System.out.println("Matching schedule(s):");
        schedules.forEach(schedule -> {
            System.out.println();
            System.out.println("DAY: " + schedule.getDay());
            System.out.println("ROOM: " + schedule.getRoom()
                    + " (" + service.locationOf(schedule.getRoom()) + ")");
            printSchedule(schedule);
        });
    }

    private String getSearchType() {
        while (true) {
            System.out.println("Search by:");
            System.out.println("1. Subject");
            System.out.println("2. Section");
            System.out.println("3. Professor");
            System.out.print("Enter search type: ");

            String input = readLineOrExit().trim();
            if (input.matches("[1-3]")) {
                return input;
            }
            showError("Invalid search type. Enter 1, 2, or 3.");
        }
    }

    private void viewDaySchedules() {
        printTitle("VIEW ALL ROOM SCHEDULES");

        String day = getDay();
        Map<String, List<Schedule>> schedulesByRoom = service.groupSchedulesByRoom(day);

        System.out.println("Schedules for all rooms on " + day + ":");
        if (schedulesByRoom.isEmpty()) {
            System.out.println("No schedules are listed for this day.");
            return;
        }

        schedulesByRoom.forEach((room, schedules) -> {
            System.out.println();
            System.out.println("==========================================");
            System.out.println("ROOM: " + room);
            System.out.println("LOCATION: " + service.locationOf(room));
            System.out.println("==========================================");
            printGroupedSchedules(schedules);
        });
    }

    private String getDay() {
        while (true) {
            System.out.println("Select day:");
            System.out.println("1. Monday");
            System.out.println("2. Tuesday");
            System.out.println("3. Wednesday");
            System.out.println("4. Thursday");
            System.out.println("5. Friday");
            System.out.println("6. Saturday");
            System.out.println();
            System.out.print("Enter day: ");

            switch (readLineOrExit().trim().toUpperCase(Locale.ROOT)) {
                case "1", "MONDAY" -> { return "MONDAY"; }
                case "2", "TUESDAY" -> { return "TUESDAY"; }
                case "3", "WEDNESDAY" -> { return "WEDNESDAY"; }
                case "4", "THURSDAY" -> { return "THURSDAY"; }
                case "5", "FRIDAY" -> { return "FRIDAY"; }
                case "6", "SATURDAY" -> { return "SATURDAY"; }
                default -> showError("Invalid day. Enter 1-6 or Monday-Saturday.");
            }
        }
    }

    private String getRoom() {
        while (true) {
            System.out.println();
            System.out.print("Enter room number (example: 4-11 or 201): ");
            String room = readLineOrExit().trim();
            if (service.isValidRoom(room)) {
                return room;
            }
            showError("Invalid room number.");
        }
    }

    private LocalTime getTime(String message) {
        while (true) {
            System.out.print(message);
            String input = readLineOrExit().trim().toUpperCase(Locale.ROOT);

            try {
                return LocalTime.parse(
                        input.replaceAll("\\s+", " "),
                        DateTimeFormatter.ofPattern("h:mm a", Locale.ROOT));
            } catch (DateTimeParseException exception) {
                showError("Invalid time. Use a format such as 7:00 AM or 12:00 PM.");
            }
        }
    }

    private String formatTime(LocalTime time) {
        return time.format(DISPLAY_TIME);
    }

    private void printSchedule(Schedule schedule) {
        System.out.println(SEPARATOR);
        System.out.println("Time      : " + formatTime(schedule.getStart())
                + " - " + formatTime(schedule.getEnd()));
        System.out.println("Subject   : " + schedule.getSubject());
        System.out.println("Section   : " + schedule.getSection());
        System.out.println("Professor : " + schedule.getProfessor());
        System.out.println();
    }

    private void printGroupedSchedules(List<Schedule> schedules) {
        Map<String, Schedule> groupedSchedules = new LinkedHashMap<>();
        Map<String, LinkedHashSet<String>> sectionsBySchedule = new LinkedHashMap<>();

        for (Schedule schedule : schedules) {
            String key = schedule.getRoom() + "|" + schedule.getStart() + "|"
                    + schedule.getEnd() + "|" + schedule.getSubject() + "|"
                    + schedule.getProfessor();
            groupedSchedules.putIfAbsent(key, schedule);
            sectionsBySchedule.computeIfAbsent(key, ignored -> new LinkedHashSet<>())
                    .add(schedule.getSection());
        }

        groupedSchedules.forEach((key, schedule) -> {
            String sections = String.join(" / ", sectionsBySchedule.get(key));
            Schedule groupedSchedule = new Schedule(
                    schedule.getDay(),
                    schedule.getRoom(),
                    schedule.getStart(),
                    schedule.getEnd(),
                    schedule.getSubject(),
                    sections,
                    schedule.getProfessor());
            printSchedule(groupedSchedule);
        });
    }

    private void printTitle(String title) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("  " + title);
        System.out.println("==========================================");
        System.out.println();
    }

    private void printRoomContext(String day, String room) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("DAY: " + day);
        System.out.println("ROOM: " + room);
        System.out.println("LOCATION: " + service.locationOf(room));
        System.out.println("==========================================");
        System.out.println();
    }

    private void pause() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        readLineOrExit();
        System.out.println();
    }

    private String readLineOrExit() {
        if (!scanner.hasNextLine()) {
            throw new EndOfInputException();
        }
        return scanner.nextLine();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void showError(String message) {
        System.out.println("\n" + message);
        sleepBeforeRetry();
        clearScreen();
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private static final class EndOfInputException extends RuntimeException {
    }
}
