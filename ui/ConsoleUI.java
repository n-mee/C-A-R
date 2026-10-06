package ui;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import model.Schedule;
import service.ScheduleService;

public class ConsoleUI {
    private final ScheduleService service;
    private final Scanner scanner;

    public ConsoleUI(ScheduleService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            showMenu();

            switch (scanner.nextLine().trim()) {
                case "1" -> checkAvailability();
                case "2" -> viewSchedule();
                case "3", "0" -> {
                    System.out.println("\nThank you for using the system!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n==========================================");
        System.out.println("       CLASSROOM AVAILABILITY SYSTEM");
        System.out.println("==========================================");
        System.out.println("1. Check Classroom Availability");
        System.out.println("2. View Classroom Schedule\n");
        System.out.println("0. Exit");
        System.out.println("==========================================");
        System.out.print("Enter your choice: ");
    }

    private void checkAvailability() {
        System.out.println("\n--- CHECK CLASSROOM AVAILABILITY ---");

        String day = getDay();
        String room = getRoom();
        String start = getTime("Enter start time (e.g. 7:00 AM): ");
        String end = getTime("Enter end time (e.g. 12:00 PM): ");
        int startMinutes = toMinutes(start);
        int endMinutes = toMinutes(end);

        if (endMinutes <= startMinutes) {
            System.out.println("Invalid time range. End time must be later than start time.");
            return;
        }

        List<Schedule> conflicts = service.findConflicts(day, room, startMinutes, endMinutes);

        System.out.println("\n------------------------------------------");
        System.out.println("DAY: " + day);
        System.out.println("ROOM: " + room);
        System.out.println("TIME: " + formatTime(start) + " - " + formatTime(end));
        System.out.println("------------------------------------------");

        if (conflicts.isEmpty()) {
            System.out.println("STATUS: AVAILABLE");
            System.out.println("There is no scheduled class/activity during this time.");
            return;
        }

        System.out.println("STATUS: OCCUPIED");
        System.out.println("\nExisting schedule(s) causing the conflict:");
        conflicts.forEach(this::printSchedule);

        List<String> availableRooms =
                service.findAvailableRooms(day, room, startMinutes, endMinutes);

        if (availableRooms.isEmpty()) {
            System.out.println("\nNo other available room was found for this time.");
        } else {
            System.out.println("\nAVAILABLE ALTERNATIVE ROOM(S):");
            availableRooms.forEach(availableRoom ->
                    System.out.println("- " + availableRoom + " (" + service.locationOf(availableRoom) + ")"));
        }
    }

    private void viewSchedule() {
        System.out.println("\n--- VIEW CLASSROOM SCHEDULE ---");

        String day = getDay();
        String room = getRoom();
        List<Schedule> schedules = service.findSchedule(day, room);

        System.out.println("\n==========================================");
        System.out.println("DAY: " + day);
        System.out.println("ROOM: " + room);
        System.out.println("LOCATION: " + service.locationOf(room));
        System.out.println("==========================================");

        if (schedules.isEmpty()) {
            System.out.println("No schedule is listed for this room on " + day + ".");
            return;
        }

        schedules.forEach(this::printSchedule);
    }

    private String getDay() {
        while (true) {
            System.out.println("\nSelect day:");
            System.out.println("1. Monday");
            System.out.println("2. Tuesday");
            System.out.println("3. Wednesday");
            System.out.println("4. Thursday");
            System.out.println("5. Friday");
            System.out.println("6. Saturday");
            System.out.print("Enter day: ");

            switch (scanner.nextLine().trim().toUpperCase(Locale.ROOT)) {
                case "1", "MONDAY" -> { return "MONDAY"; }
                case "2", "TUESDAY" -> { return "TUESDAY"; }
                case "3", "WEDNESDAY" -> { return "WEDNESDAY"; }
                case "4", "THURSDAY" -> { return "THURSDAY"; }
                case "5", "FRIDAY" -> { return "FRIDAY"; }
                case "6", "SATURDAY" -> { return "SATURDAY"; }
                default -> System.out.println("Invalid day. Enter 1-6 or Monday-Saturday.");
            }
        }
    }

    private String getRoom() {
        while (true) {
            System.out.print("\nEnter room number (example: 4-11 or 201): ");
            String room = scanner.nextLine().trim();
            if (service.isValidRoom(room)) {
                return room;
            }
            System.out.println("Invalid room number.");
        }
    }

    private String getTime(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

            if (input.matches("(0?[1-9]|1[0-2]):[0-5][0-9]\\s*(AM|PM)")) {
                String[] parts = input.replaceAll("\\s+", " ").split(" ");
                String[] hourMinute = parts[0].split(":");
                int hour = Integer.parseInt(hourMinute[0]);
                int minute = Integer.parseInt(hourMinute[1]);

                if (parts[1].equals("AM")) {
                    if (hour == 12) hour = 0;
                } else if (hour != 12) {
                    hour += 12;
                }

                return String.format("%02d:%02d", hour, minute);
            }

            System.out.println("Invalid time. Use a format such as 7:00 AM or 12:00 PM.");
        }
    }

    private int toMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private String formatTime(String time) {
        int total = toMinutes(time);
        int hour24 = total / 60;
        int minute = total % 60;
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;
        return String.format("%d:%02d %s", hour12, minute, hour24 >= 12 ? "PM" : "AM");
    }

    private void printSchedule(Schedule schedule) {
        System.out.println("------------------------------------------");
        System.out.println("Time      : " + formatTime(schedule.getStart())
                + " - " + formatTime(schedule.getEnd()));
        System.out.println("Subject   : " + schedule.getSubject());
        System.out.println("Section   : " + schedule.getSection());
        System.out.println("Professor : " + schedule.getProfessor());
    }
}
