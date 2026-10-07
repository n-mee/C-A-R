package ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import repository.RoomRepository;

final class JavaFxScreenFactory {
    static final String TITLE_STYLE =
            "-fx-font-size: 22px; -fx-font-weight: bold;";
    static final String PRIMARY_BUTTON_STYLE =
            "-fx-font-size: 14px; -fx-padding: 9 14 9 14;";

    private final RoomRepository roomRepository;

    JavaFxScreenFactory(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    ComboBox<String> createDayBox() {
        ComboBox<String> dayBox = new ComboBox<>();
        dayBox.getItems().addAll(
                "MONDAY", "TUESDAY", "WEDNESDAY",
                "THURSDAY", "FRIDAY", "SATURDAY");
        dayBox.setPromptText("Select day");
        return dayBox;
    }

    ComboBox<String> createRoomBox() {
        ComboBox<String> roomBox = new ComboBox<>();
        roomRepository.findAll().stream()
                .map(model.Room::number)
                .forEach(roomBox.getItems()::add);
        roomBox.setPromptText("Select room");
        return roomBox;
    }

    TextArea createResultArea() {
        TextArea result = new TextArea();
        result.setEditable(false);
        result.setWrapText(true);
        result.setPrefHeight(420);
        result.setMaxHeight(Double.MAX_VALUE);
        result.setStyle("-fx-font-family: monospace; -fx-font-size: 13px;");
        return result;
    }

    VBox createScreen(Node... controls) {
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

    Button createBackButton(Runnable action) {
        Button backButton = new Button("Back to dashboard");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setStyle(PRIMARY_BUTTON_STYLE);
        backButton.setOnAction(event -> action.run());
        return backButton;
    }
}
