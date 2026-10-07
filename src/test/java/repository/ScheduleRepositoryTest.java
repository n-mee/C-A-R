package repository;

import java.util.List;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.Schedule;

class ScheduleRepositoryTest {

    @Test
    void shouldLoadSchedules() {
        ScheduleRepository repository = new ScheduleRepository();

        List<Schedule> schedules = repository.findAll();

        assertFalse(schedules.isEmpty());
    }

    @Test
    void shouldParseScheduleFields() {
        ScheduleRepository repository = new ScheduleRepository();

        Schedule schedule = repository.findAll().stream()
            .filter(item -> item.getDay().equals("MONDAY") && item.getRoom().equals("4-4"))
            .findFirst()
            .orElseThrow();


        assertEquals("MONDAY", schedule.getDay());
        assertEquals("4-4", schedule.getRoom());
        assertEquals(LocalTime.of(7, 0), schedule.getStart());
        assertEquals(LocalTime.of(12, 0), schedule.getEnd());
        assertEquals("NSTP 1", schedule.getSubject());
    }

    @Test
    void shouldNormalizeAfternoonTimes() {
        ScheduleRepository repository = new ScheduleRepository();

        Schedule schedule = repository.findAll().stream()
                .filter(item ->
                        item.getDay().equals("MONDAY")
                                && item.getRoom().equals("4-4")
                                && item.getStart().getHour() == 13
                                && item.getStart().getMinute() == 30)
                .findFirst()
                .orElseThrow();

        assertEquals(LocalTime.of(16, 0), schedule.getEnd());
    }

    @Test
    void shouldRejectRowsWithIncorrectFieldCount() {
        ScheduleRepository repository =
                new ScheduleRepository("/malformed-schedules.csv");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                repository::findAll);

        assertTrue(exception.getMessage().contains("expected 7 fields"));
    }

    @Test
    void shouldRejectInvalidTimeValues() {
        ScheduleRepository repository =
                new ScheduleRepository("/invalid-time-schedules.csv");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                repository::findAll);

        assertTrue(exception.getMessage() != null);
    }

    @Test
    void shouldRejectMissingScheduleResource() {
        ScheduleRepository repository =
                new ScheduleRepository("/does-not-exist.csv");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                repository::findAll);

        assertTrue(exception.getMessage().contains("resource not found"));
    }

    @Test
    void shouldRejectUnknownDay() {
        ScheduleRepository repository = new ScheduleRepository("/invalid-day-schedules.csv");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                repository::findAll);

        assertTrue(exception.getMessage().contains("unknown day"));
    }

    @Test
    void shouldRejectBlankFields() {
        ScheduleRepository repository = new ScheduleRepository("/blank-field-schedules.csv");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                repository::findAll);

        assertTrue(exception.getMessage().contains("is blank"));
    }

    @Test
    void shouldRejectInvalidTimeRange() {
        ScheduleRepository repository = new ScheduleRepository("/invalid-range-schedules.csv");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                repository::findAll);

        assertTrue(exception.getMessage().contains("start time must be before end time"));
    }
}