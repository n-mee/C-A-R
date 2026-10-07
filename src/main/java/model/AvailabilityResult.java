package model;

import java.util.List;

public record AvailabilityResult(
        boolean available,
        List<Schedule> conflicts,
        List<Room> alternatives) {
}
