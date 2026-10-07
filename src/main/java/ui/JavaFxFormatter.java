package ui;

import model.AvailabilityResult;
import model.Room;
import model.Schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

final class JavaFxFormatter {
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("h:mm a");

    private JavaFxFormatter() {
    }

    static String availability(
            AvailabilityResult availability,
            String day,
            String room,
            LocalTime start,
            LocalTime end) {
        String time = formatTime(start) + " - " + formatTime(end);
        if (availability.available()) {
            return String.format("%s, room %s, %s is AVAILABLE.%n"
                    + "No scheduled class or activity overlaps this time.",
                    day, room, time);
        }

        StringBuilder message = new StringBuilder(String.format(
                "%s, room %s, %s is OCCUPIED.%nConflicts:%n",
                day, room, time));
        availability.conflicts().forEach(schedule -> message.append("- ")
                .append(schedule.getSubject())
                .append(" (")
                .append(schedule.getSection())
                .append(")")
                .append(System.lineSeparator()));

        List<Room> alternatives = availability.alternatives();
        if (alternatives.isEmpty()) {
            message.append("No alternative room was found.");
        } else {
            message.append("Alternative rooms: ");
            message.append(alternatives.stream()
                    .map(Room::number)
                    .reduce((first, second) -> first + ", " + second)
                    .orElse("None"));
        }
        return message.toString();
    }

    static String roomSchedule(
            String day, String room, String location, List<Schedule> schedules) {
        return "DAY: " + day + "\nROOM: " + room + " ("
                + location + ")\n\n" + scheduleList(schedules);
    }

    static String searchResults(
            List<Schedule> schedules, Map<String, String> locations) {
        StringBuilder output = new StringBuilder("Matching schedules:\n");
        for (Schedule schedule : schedules) {
            output.append("\nDAY: ").append(schedule.getDay())
                    .append("\nROOM: ").append(schedule.getRoom())
                    .append(" (").append(locations.getOrDefault(
                            schedule.getRoom(), "Unknown Location"))
                    .append(")\n")
                    .append(schedule(schedule));
        }
        return output.toString();
    }

    static String allRoomSchedules(
            String day, Map<String, List<Schedule>> schedules, Map<String, String> locations) {
        StringBuilder output = new StringBuilder(
                "Schedules for all rooms on " + day + ":\n");
        schedules.forEach((room, roomSchedules) -> output
                .append("\nROOM: ").append(room)
                .append(" (").append(locations.getOrDefault(room, "Unknown Location"))
                .append(")\n")
                .append(scheduleList(roomSchedules)));
        return output.toString();
    }

    static String scheduleList(List<Schedule> schedules) {
        StringBuilder output = new StringBuilder();
        schedules.forEach(schedule -> output.append(schedule(schedule)));
        return output.toString();
    }

    private static String schedule(Schedule schedule) {
        return "Time: " + formatTime(schedule.getStart()) + " - "
                + formatTime(schedule.getEnd()) + "\n"
                + "Subject: " + schedule.getSubject() + "\n"
                + "Section: " + schedule.getSection() + "\n"
                + "Professor: " + schedule.getProfessor() + "\n\n";
    }

    private static String formatTime(LocalTime time) {
        return time.format(DISPLAY_TIME);
    }
}
