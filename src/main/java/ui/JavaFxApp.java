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
    private static final DateTimeFormatter INPUT_TIME =
            DateTimeFormatter.ofPattern("h:mm a");

    private final ScheduleService service =
            new ScheduleService(new ScheduleRepository(), new RoomRepository());
    private final RoomRepository roomRepository = new RoomRepository();
    private final JavaFxScreenFactory screenFactory =
            new JavaFxScreenFactory(roomRepository);
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
        title.setStyle(JavaFxScreenFactory.TITLE_STYLE);

        ComboBox<String> dayBox = screenFactory.createDayBox();
        ComboBox<String> roomBox = screenFactory.createRoomBox();

        TextField startField = new TextField();
        startField.setPromptText("Start time (e.g. 8:00 AM)");

        TextField endField = new TextField();
        endField.setPromptText("End time (e.g. 9:00 AM)");

        Label result = new Label("Enter the details and select Check.");
        result.setWrapText(true);

        Button checkButton = new Button("Check availability");
        checkButton.setMaxWidth(Double.MAX_VALUE);
        checkButton.setStyle(JavaFxScreenFactory.PRIMARY_BUTTON_STYLE);
        checkButton.setOnAction(event -> checkAvailability(
                dayBox, roomBox, startField, endField, result));

        Button backButton = screenFactory.createBackButton(this::showDashboard);

        VBox layout = new VBox(
                14, title, dayBox, roomBox, startField, endField,
                checkButton, result, backButton);
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
            result.setText(JavaFxFormatter.availability(
                    availability, dayBox.getValue(), roomBox.getValue(), start, end));
        } catch (DateTimeParseException exception) {
            result.setStyle("-fx-text-fill: #b71c1c;");
            result.setText("Invalid time. Use a format such as 8:00 AM.");
        }
    }

    private void showRoomSchedule() {
        Label title = new Label("View Room Schedule");
        title.setStyle(JavaFxScreenFactory.TITLE_STYLE);

        ComboBox<String> dayBox = screenFactory.createDayBox();
        ComboBox<String> roomBox = screenFactory.createRoomBox();
        TextArea result = screenFactory.createResultArea();
        result.setText("Select a day and room.");

        Button viewButton = new Button("View schedule");
        viewButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setStyle(JavaFxScreenFactory.PRIMARY_BUTTON_STYLE);
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

            result.setText(JavaFxFormatter.roomSchedule(
                    dayBox.getValue(), roomBox.getValue(),
                    service.locationOf(roomBox.getValue()), schedules));
        });

        setScene("View Room Schedule", screenFactory.createScreen(
                title, dayBox, roomBox, viewButton, result,
                screenFactory.createBackButton(this::showDashboard)), 620, 620);
    }

    private void showSearch() {
        Label title = new Label("Search Schedules");
        title.setStyle(JavaFxScreenFactory.TITLE_STYLE);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Subject", "Section", "Professor");
        typeBox.setPromptText("Search by");

        TextField queryField = new TextField();
        queryField.setPromptText("Enter search text");

        TextArea result = screenFactory.createResultArea();
        result.setText("Choose a search field and enter a query.");

        Button searchButton = new Button("Search");
        searchButton.setMaxWidth(Double.MAX_VALUE);
        searchButton.setStyle(JavaFxScreenFactory.PRIMARY_BUTTON_STYLE);
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

            result.setText(JavaFxFormatter.searchResults(schedules, roomLocations()));
        });

        setScene("Search Schedules", screenFactory.createScreen(
                title, typeBox, queryField, searchButton, result,
                screenFactory.createBackButton(this::showDashboard)), 700, 650);
    }

    private void showDaySchedules() {
        Label title = new Label("View All Room Schedules");
        title.setStyle(JavaFxScreenFactory.TITLE_STYLE);

        ComboBox<String> dayBox = screenFactory.createDayBox();
        TextArea result = screenFactory.createResultArea();
        result.setText("Select a day.");

        Button viewButton = new Button("View schedules");
        viewButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setStyle(JavaFxScreenFactory.PRIMARY_BUTTON_STYLE);
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

            result.setText(JavaFxFormatter.allRoomSchedules(
                    dayBox.getValue(), schedules, roomLocations()));
        });

        setScene("View All Room Schedules", screenFactory.createScreen(
                title, dayBox, viewButton, result,
                screenFactory.createBackButton(this::showDashboard)), 760, 700);
    }

    private Map<String, String> roomLocations() {
        return roomRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Room::number, Room::location));
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
