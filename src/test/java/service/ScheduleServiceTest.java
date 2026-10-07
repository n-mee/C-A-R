package service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.Schedule;
import repository.ScheduleRepository;
import model.Room;

class ScheduleServiceTest {

    private ScheduleService createService() {
        return new ScheduleService(new ScheduleRepository());
    }

    @Test
    void shouldFindSchedulesForRoomAndDay() {
        ScheduleService service = createService();

        List<Schedule> schedules =
                service.findSchedule("MONDAY", "4-4");

        assertFalse(schedules.isEmpty());

        assertTrue(schedules.stream()
                .allMatch(schedule ->
                        schedule.getDay().equals("MONDAY")
                                && schedule.getRoom().equals("4-4")));
    }

    @Test
    void shouldReportRoomAsAvailableWhenTimeOverlapsClass() {
        ScheduleService service = createService();

        boolean available = service.isRoomAvailable(
                "MONDAY", "4-4", LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertFalse(available);
    }

    @Test
    void shouldReportRoomAsAvailableWhenThereIsNoOverlap() {
        ScheduleService service = createService();

        boolean available = service.isRoomAvailable(
                "MONDAY", "4-4", LocalTime.of(12, 0), LocalTime.of(13, 0));

        assertTrue(available);
    }

    @Test
    void shouldAllowRequestThatEndsWhenClassStarts() {
        ScheduleService service = createService();

        boolean available = service.isRoomAvailable(
                "MONDAY", "4-4", LocalTime.of(6, 0), LocalTime.of(7, 0));

        assertTrue(available);
    }

    @Test
    void shouldRejectInvalidRoom() {
        ScheduleService service = createService();

        assertFalse(service.isValidRoom("999"));
    }

    @Test
    void shouldAcceptValidRoom() {
        ScheduleService service = createService();

        assertTrue(service.isValidRoom("4-4"));
    }

    @Test
    void shouldReturnConflictingSchedules() {
        ScheduleService service = createService();

        List<Schedule> conflicts = service.findConflicts(
                "MONDAY", "4-4", LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertFalse(conflicts.isEmpty());

        assertTrue(conflicts.stream().allMatch(schedule -> schedule.getDay().equals("MONDAY") && schedule.getRoom().equals("4-4")));
    }

    @Test
    void shouldOnlyReturnAlternativeRoomsFromSameArea() {
        ScheduleService service = createService();

        List<Room> alternatives = service.findAvailableRooms(
                "MONDAY", "4-4", LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertFalse(alternatives.isEmpty());

        assertTrue(alternatives.stream().allMatch(room -> room.number().startsWith("4-")));
    }

    @Test
    void shouldNotIncludeSelectedRoomInAlternatives() {
        ScheduleService service = createService();

        List<Room> alternatives = service.findAvailableRooms(
                "MONDAY", "4-4", LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertFalse(alternatives.stream().anyMatch(room -> room.number().equals("4-4")));
    }

    @Test
    void shouldReturnOnlyAvailableAlternativeRooms() {
        ScheduleService service = createService();

        List<Room> alternatives = service.findAvailableRooms(
                "MONDAY", "4-4", LocalTime.of(8, 0), LocalTime.of(9, 0));

        for (Room room : alternatives) {
            assertTrue(service.isRoomAvailable(
                    "MONDAY", room.number(), LocalTime.of(8, 0), LocalTime.of(9, 0)));
        }
    }

    @Test
    void shouldSearchSchedulesBySubject() {
        ScheduleService service = createService();

        List<Schedule> results = service.searchBySubject("forensic chemistry");

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .allMatch(schedule -> schedule.getSubject().toLowerCase().contains("forensic chemistry")));
    }

    @Test
    void shouldSearchSchedulesByProfessor() {
        ScheduleService service = createService();

        List<Schedule> results = service.searchByProfessor("pahitong");

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .allMatch(schedule -> schedule.getProfessor().toLowerCase().contains("pahitong")));
    }

    @Test
    void shouldSearchSchedulesBySection() {
        ScheduleService service = createService();

        List<Schedule> results = service.searchBySection("CRIM 1B");

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .allMatch(schedule -> schedule.getSection().toLowerCase().contains("crim 1b")));
    }

    @Test
    void shouldReturnAllSchedulesForDayGroupedByRoom() {
        ScheduleService service = createService();

        Map<String, List<Schedule>> schedules = service.groupSchedulesByRoom("MONDAY");

        assertFalse(schedules.isEmpty());
        assertTrue(schedules.containsKey("4-4"));
        assertTrue(schedules.values().stream()
                .flatMap(List::stream)
                .allMatch(schedule -> schedule.getDay().equals("MONDAY")));
    }

    @Test
    void shouldReturnEmptyResultsForBlankSearch() {
        ScheduleService service = createService();

        assertTrue(service.searchBySubject("   ").isEmpty());
    }
}