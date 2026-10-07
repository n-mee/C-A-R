package repository;

import model.Room;

import java.util.List;
import java.util.stream.Stream;

public class RoomRepository {
    private static final List<Room> ROOMS = Stream.concat(
            Stream.of(rooms("3-", "3rd Floor College Building", 1, 12),
                    rooms("4-", "4th Floor College Building", 1, 14))
                    .flatMap(java.util.Arrays::stream),
            Stream.of(
                    new Room("201", "High School Building"),
                    new Room("202", "High School Building"),
                    new Room("203", "High School Building"),
                    new Room("205", "High School Building"),
                    new Room("206", "High School Building"),
                    new Room("207", "High School Building"),
                    new Room("209", "High School Building"),
                    new Room("210", "High School Building")))
            .toList();

    public List<Room> findAll() {
        return ROOMS;
    }

    private static Room[] rooms(String prefix, String location, int start, int end) {
        Room[] rooms = new Room[end - start + 1];
        for (int number = start; number <= end; number++) {
            rooms[number - start] = new Room(prefix + number, location);
        }
        return rooms;
    }
}
