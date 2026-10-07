package service;

import model.AvailabilityResult;
import model.Room;
import model.Schedule;
import repository.RoomRepository;
import repository.ScheduleRepository;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Comparator;

public class ScheduleService {
    private final ScheduleRepository repository;
    private final RoomRepository roomRepository;

    public ScheduleService(ScheduleRepository repository) {
        this(repository, new RoomRepository());
    }

    public ScheduleService(ScheduleRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    public List<Schedule> findSchedule(String day, String room) {
        return repository.findAll()
                .stream()
                .filter(s -> s.getDay().equals(day))
                .filter(s -> s.getRoom().equals(room))
                .sorted(java.util.Comparator.comparing(Schedule::getStart))
                .toList();
    }

    public List<Schedule> findSchedulesForDay(String day) {
        return repository.findAll().stream()
                .filter(schedule -> schedule.getDay().equals(day))
                .sorted(Comparator.comparing(Schedule::getRoom)
                        .thenComparing(Schedule::getStart))
                .toList();
    }

    public Map<String, List<Schedule>> groupSchedulesByRoom(String day) {
        Map<String, List<Schedule>> grouped = new LinkedHashMap<>();
        for (Schedule schedule : findSchedulesForDay(day)) {
            grouped.computeIfAbsent(schedule.getRoom(), ignored -> new java.util.ArrayList<>())
                    .add(schedule);
        }
        return grouped;
    }

    public List<Schedule> searchBySubject(String query) {
        return search(query, Schedule::getSubject);
    }

    public List<Schedule> searchBySection(String query) {
        return search(query, Schedule::getSection);
    }

    public List<Schedule> searchByProfessor(String query) {
        return search(query, Schedule::getProfessor);
    }

    public AvailabilityResult checkAvailability(
            String day, String room, LocalTime requestedStart, LocalTime requestedEnd) {
        List<Schedule> conflicts = findConflicts(day, room, requestedStart, requestedEnd);
        List<Room> alternatives = conflicts.isEmpty()
                ? List.of()
                : findAvailableRooms(day, room, requestedStart, requestedEnd);
        return new AvailabilityResult(conflicts.isEmpty(), conflicts, alternatives);
    }

    public boolean isRoomAvailable(
            String day, String room, LocalTime requestedStart, LocalTime requestedEnd) {
        return findConflicts(day, room, requestedStart, requestedEnd).isEmpty();
    }

    public List<Schedule> findConflicts(
            String day, String room, LocalTime requestedStart, LocalTime requestedEnd) {
        return repository.findAll().stream()
                .filter(schedule -> schedule.getDay().equals(day))
                .filter(schedule -> schedule.getRoom().equals(room))
                .filter(schedule -> overlaps(
                        requestedStart,
                        requestedEnd,
                        schedule.getStart(),
                        schedule.getEnd()))
                .toList();
    }

    public List<Room> findAvailableRooms(
            String day, String selectedRoom,
            LocalTime requestedStart, LocalTime requestedEnd) {
        return roomRepository.findAll().stream()
                .filter(room -> !room.number().equals(selectedRoom))
                .filter(room -> sameArea(room.number(), selectedRoom))
                .filter(room -> isRoomAvailable(day, room.number(), requestedStart, requestedEnd))
                .toList();
    }

    public boolean isValidRoom(String room) {
        return roomRepository.findAll().stream()
                .anyMatch(knownRoom -> knownRoom.number().equals(room));
    }

    public String locationOf(String room) {
        return roomRepository.findAll().stream()
                .filter(knownRoom -> knownRoom.number().equals(room))
                .map(Room::location)
                .findFirst()
                .orElse("Unknown Location");
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

    private boolean overlaps(
            LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private List<Schedule> search(String query, java.util.function.Function<Schedule, String> field) {
        String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);
        if (normalizedQuery.isEmpty()) {
            return List.of();
        }

        return repository.findAll().stream()
                .filter(schedule -> field.apply(schedule)
                        .toLowerCase(Locale.ROOT)
                        .contains(normalizedQuery))
                .sorted(Comparator.comparing(Schedule::getDay)
                        .thenComparing(Schedule::getStart)
                        .thenComparing(Schedule::getRoom))
                .toList();
    }
}
