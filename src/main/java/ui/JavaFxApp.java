package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AvailabilityResult;
import model.Room;
import repository.RoomRepository;
import repository.ScheduleRepository;
import service.ScheduleService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class JavaFxApp extends Application {
    private static final String TITLE_STYLE =
            "-fx-font-size: 22px; -fx-font-weight: bold;";
    private static final String PRIMARY_BUTTON_STYLE =
            "-fx-font-size: 14px; -fx-padding: 9 14 9 14;";
    private static final DateTimeFormatter INPUT_TIME =
            DateTimeFormatter.ofPattern("h:mm a");

    private final ScheduleService service =
            new ScheduleService(new ScheduleRepository(), new RoomRepository());
    private final RoomRepository roomRepository = new RoomRepository();
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        showDashboard();
    }

    private void showDashboard() {
        Label title = new Label("Classroom Availability System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Choose an action to get started.");
        Label dataStatus = new Label(
                "Loaded schedule entries: " + service.findSchedulesForDay("MONDAY").size());

        Button availabilityButton = createActionButton(
                "Check classroom availability", event -> showAvailability());
        Button roomScheduleButton = createActionButton(
                "View room schedule", event -> showRoomSchedule());
        Button searchButton = createActionButton(
                "Search schedules", event -> showSearch());
        Button dayScheduleButton = createActionButton(
                "View all room schedules", event -> showDaySchedules());

        VBox layout = new VBox(
                16,
                title,
                subtitle,
                dataStatus,
                availabilityButton,
                roomScheduleButton,
                searchButton,
                dayScheduleButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(32));

        setScene("Classroom Availability System", layout, 520, 420);
    }

    private void showAvailability() {
        Label title = new Label("Check Classroom Availability");
        title.setStyle(TITLE_STYLE);

        ComboBox<String> dayBox = new ComboBox<>();
        dayBox.getItems().addAll(
                "MONDAY", "TUESDAY", "WEDNESDAY",
                "THURSDAY", "FRIDAY", "SATURDAY");
        dayBox.setPromptText("Select day");

        ComboBox<String> roomBox = new ComboBox<>();
        roomRepository.findAll().stream()
                .map(Room::number)
                .forEach(roomBox.getItems()::add);
        roomBox.setPromptText("Select room");

        TextField startField = new TextField();
        startField.setPromptText("Start time (e.g. 8:00 AM)");

        TextField endField = new TextField();
        endField.setPromptText("End time (e.g. 9:00 AM)");

        Label result = new Label("Enter the details and select Check.");
        result.setWrapText(true);
        result.setMaxWidth(Double.MAX_VALUE);

        Button checkButton = new Button("Check availability");
        checkButton.setMaxWidth(Double.MAX_VALUE);
        checkButton.setStyle(PRIMARY_BUTTON_STYLE);
        checkButton.setOnAction(event -> checkAvailability(
                dayBox, roomBox, startField, endField, result));

        Button backButton = new Button("Back to dashboard");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(event -> showDashboard());

        VBox layout = new VBox(
                14, title, dayBox, roomBox, startField, endField,
                checkButton, result, backButton);
        layout.setPadding(new Insets(32));
        layout.setAlignment(Pos.CENTER);

        setScene("Check Classroom Availability", layout, 520, 520);
    }

    private void checkAvailability(
            ComboBox<String> dayBox,
            ComboBox<String> roomBox,
            TextField startField,
            TextField endField,
            Label result) {
        if (dayBox.getValue() == null || roomBox.getValue() == null
                || startField.getText().isBlank() || endField.getText().isBlank()) {
            result.setText("Please select a day and room and enter both times.");
            return;
        }

        try {
            LocalTime start = LocalTime.parse(startField.getText().trim(), INPUT_TIME);
            LocalTime end = LocalTime.parse(endField.getText().trim(), INPUT_TIME);

            if (!end.isAfter(start)) {
                result.setText("End time must be later than start time.");
                return;
            }

            AvailabilityResult availability = service.checkAvailability(
                    dayBox.getValue(), roomBox.getValue(), start, end);
            result.setStyle(availability.available()
                    ? "-fx-text-fill: #1b5e20;"
                    : "-fx-text-fill: #b71c1c;");
            result.setText(formatAvailability(
                    availability, dayBox.getValue(), roomBox.getValue(), start, end));
        } catch (DateTimeParseException exception) {
            result.setStyle("-fx-text-fill: #b71c1c;");
            result.setText("Invalid time. Use a format such as 8:00 AM.");
        }
    }

    private String formatAvailability(
            AvailabilityResult availability,
            String day,
            String room,
            LocalTime start,
            LocalTime end) {
        String time = start.format(INPUT_TIME) + " - " + end.format(INPUT_TIME);
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

    private void showRoomSchedule() {
        Label title = new Label("View Room Schedule");
        title.setStyle(TITLE_STYLE);

        ComboBox<String> dayBox = createDayBox();
        ComboBox<String> roomBox = createRoomBox();
        TextArea result = createResultArea();
        result.setText("Select a day and room.");

        Button viewButton = new Button("View schedule");
        viewButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setStyle(PRIMARY_BUTTON_STYLE);
        viewButton.setOnAction(event -> {
            if (dayBox.getValue() == null || roomBox.getValue() == null) {
                result.setText("Please select both a day and room.");
                return;
            }

            List<model.Schedule> schedules =
                    service.findSchedule(dayBox.getValue(), roomBox.getValue());
            if (schedules.isEmpty()) {
                result.setText("No schedule is listed for this room on "
                        + dayBox.getValue() + ".");
                return;
            }

            result.setText(formatSchedules(
                    dayBox.getValue(), roomBox.getValue(), schedules));
        });

        setScene("View Room Schedule", createScreen(
                title, dayBox, roomBox, viewButton, result, backButton()), 620, 620);
    }

    private void showSearch() {
        Label title = new Label("Search Schedules");
        title.setStyle(TITLE_STYLE);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Subject", "Section", "Professor");
        typeBox.setPromptText("Search by");

        TextField queryField = new TextField();
        queryField.setPromptText("Enter search text");

        TextArea result = createResultArea();
        result.setText("Choose a search field and enter a query.");

        Button searchButton = new Button("Search");
        searchButton.setMaxWidth(Double.MAX_VALUE);
        searchButton.setStyle(PRIMARY_BUTTON_STYLE);
        searchButton.setOnAction(event -> {
            if (typeBox.getValue() == null || queryField.getText().isBlank()) {
                result.setText("Please choose a search field and enter a query.");
                return;
            }

            List<model.Schedule> schedules = switch (typeBox.getValue()) {
                case "Subject" -> service.searchBySubject(queryField.getText());
                case "Section" -> service.searchBySection(queryField.getText());
                case "Professor" -> service.searchByProfessor(queryField.getText());
                default -> List.of();
            };

            if (schedules.isEmpty()) {
                result.setText("No schedules matched your search.");
                return;
            }

            result.setText(formatSearchResults(schedules));
        });

        setScene("Search Schedules", createScreen(
                title, typeBox, queryField, searchButton, result, backButton()), 700, 650);
    }

    private void showDaySchedules() {
        Label title = new Label("View All Room Schedules");
        title.setStyle(TITLE_STYLE);

        ComboBox<String> dayBox = createDayBox();
        TextArea result = createResultArea();
        result.setText("Select a day.");

        Button viewButton = new Button("View schedules");
        viewButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setStyle(PRIMARY_BUTTON_STYLE);
        viewButton.setOnAction(event -> {
            if (dayBox.getValue() == null) {
                result.setText("Please select a day.");
                return;
            }

            Map<String, List<model.Schedule>> schedules =
                    service.groupSchedulesByRoom(dayBox.getValue());
            if (schedules.isEmpty()) {
                result.setText("No schedules are listed for " + dayBox.getValue() + ".");
                return;
            }

            StringBuilder output = new StringBuilder(
                    "Schedules for all rooms on " + dayBox.getValue() + ":\n");
            schedules.forEach((room, roomSchedules) -> output
                    .append("\nROOM: ").append(room)
                    .append(" (").append(service.locationOf(room)).append(")\n")
                    .append(formatScheduleList(roomSchedules)));
            result.setText(output.toString());
        });

        setScene("View All Room Schedules", createScreen(
                title, dayBox, viewButton, result, backButton()), 760, 700);
    }

    private ComboBox<String> createDayBox() {
        ComboBox<String> dayBox = new ComboBox<>();
        dayBox.getItems().addAll(
                "MONDAY", "TUESDAY", "WEDNESDAY",
                "THURSDAY", "FRIDAY", "SATURDAY");
        dayBox.setPromptText("Select day");
        return dayBox;
    }

    private ComboBox<String> createRoomBox() {
        ComboBox<String> roomBox = new ComboBox<>();
        roomRepository.findAll().stream()
                .map(Room::number)
                .forEach(roomBox.getItems()::add);
        roomBox.setPromptText("Select room");
        return roomBox;
    }

    private TextArea createResultArea() {
        TextArea result = new TextArea();
        result.setEditable(false);
        result.setWrapText(true);
        result.setPrefHeight(420);
        result.setMaxHeight(Double.MAX_VALUE);
        result.setStyle("-fx-font-family: monospace; -fx-font-size: 13px;");
        return result;
    }

    private VBox createScreen(Node... controls) {
        VBox layout = new VBox(14);
        layout.getChildren().addAll(controls);
        layout.setPadding(new Insets(32));
        layout.setFillWidth(true);
        layout.setStyle("-fx-background-color: #f7f9fc;");
        for (Node control : controls) {
            if (control instanceof javafx.scene.layout.Region region) {
                region.setMaxWidth(Double.MAX_VALUE);
            }
        }
        return layout;
    }

    private Button backButton() {
        Button backButton = new Button("Back to dashboard");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setStyle(PRIMARY_BUTTON_STYLE);
        backButton.setOnAction(event -> showDashboard());
        return backButton;
    }

    private String formatSchedules(
            String day, String room, List<model.Schedule> schedules) {
        return "DAY: " + day + "\nROOM: " + room + " ("
                + service.locationOf(room) + ")\n\n"
                + formatScheduleList(schedules);
    }

    private String formatSearchResults(List<model.Schedule> schedules) {
        StringBuilder output = new StringBuilder("Matching schedules:\n");
        for (model.Schedule schedule : schedules) {
            output.append("\nDAY: ").append(schedule.getDay())
                    .append("\nROOM: ").append(schedule.getRoom())
                    .append(" (").append(service.locationOf(schedule.getRoom()))
                    .append(")\n")
                    .append(formatSchedule(schedule));
        }
        return output.toString();
    }

    private String formatScheduleList(List<model.Schedule> schedules) {
        StringBuilder output = new StringBuilder();
        schedules.forEach(schedule -> output.append(formatSchedule(schedule)));
        return output.toString();
    }

    private String formatSchedule(model.Schedule schedule) {
        return "Time: " + formatTime(schedule.getStart()) + " - "
                + formatTime(schedule.getEnd()) + "\n"
                + "Subject: " + schedule.getSubject() + "\n"
                + "Section: " + schedule.getSection() + "\n"
                + "Professor: " + schedule.getProfessor() + "\n\n";
    }

    private String formatTime(LocalTime time) {
        return time.format(INPUT_TIME);
    }

    private void setScene(String title, VBox layout, double width, double height) {
        stage.setTitle(title);
        stage.setScene(new Scene(layout, width, height));
        stage.setMinWidth(460);
        stage.setMinHeight(400);
        stage.show();
    }

    private Button createActionButton(
            String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(action);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
