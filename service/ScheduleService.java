package service;

import model.Schedule;
import repository.ScheduleRepository;

import java.util.List;

public class ScheduleService {
    private static final String[] ROOMS = {
        "3-1", "3-2", "3-3", "3-4", "3-5", "3-6", "3-7", "3-8", "3-9", "3-10", "3-11", "3-12",
        "4-1", "4-2", "4-3", "4-4", "4-5", "4-6", "4-7", "4-8", "4-9", "4-10", "4-11", "4-12",
        "4-13", "4-14", "201", "202", "203", "205", "206", "207", "209", "210"
    };

    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    public List<Schedule> findSchedule(String day, String room) {
        return repository.findAll()
                .stream()
                .filter(s -> s.getDay().equals(day))
                .filter(s -> s.getRoom().equals(room))
                .toList();
    }

    public boolean isRoomAvailable(String day, String room, int requestedStart, int requestedEnd) {
        return findConflicts(day, room, requestedStart, requestedEnd).isEmpty();
    }

    public List<Schedule> findConflicts(
            String day, String room, int requestedStart, int requestedEnd) {
        return repository.findAll().stream()
                .filter(schedule -> schedule.getDay().equals(day))
                .filter(schedule -> schedule.getRoom().equals(room))
                .filter(schedule -> overlaps(
                        requestedStart,
                        requestedEnd,
                        toMinutes(schedule.getStart()),
                        toMinutes(schedule.getEnd())))
                .toList();
    }

    public List<String> findAvailableRooms(
            String day, String selectedRoom, int requestedStart, int requestedEnd) {
        return java.util.Arrays.stream(ROOMS)
                .filter(room -> !room.equals(selectedRoom))
                .filter(room -> sameArea(room, selectedRoom))
                .filter(room -> isRoomAvailable(day, room, requestedStart, requestedEnd))
                .toList();
    }

    public boolean isValidRoom(String room) {
        return java.util.Arrays.asList(ROOMS).contains(room);
    }

    public String locationOf(String room) {
        if (room.startsWith("3-")) {
            return "3rd Floor College Building";
        }
        if (room.startsWith("4-")) {
            return "4th Floor College Building";
        }
        if (isHighSchoolRoom(room)) {
            return "High School Building";
        }
        return "Unknown Location";
    }

    private boolean sameArea(String room1, String room2) {
        if (room1.startsWith("3-") && room2.startsWith("3-")) {
            return true;
        }
        if (room1.startsWith("4-") && room2.startsWith("4-")) {
            return true;
        }
        return isHighSchoolRoom(room1) && isHighSchoolRoom(room2);
    }

    private boolean isHighSchoolRoom(String room) {
        return room.matches("\\d{3}");
    }

    private boolean overlaps(int start1, int end1, int start2, int end2) {
        return start1 < end2 && end1 > start2;
    }

    private int toMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}
