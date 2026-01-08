package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.ProgramDAO;
import com.example.energyconnectjavafx.Model.Program;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.Label;

public class ProgramController extends NavController {

    @FXML private VBox programList;
    @FXML private ChoiceBox<String> programChoiceBox;

    public void initialize() {
        programChoiceBox.getItems().addAll(
                "TITLE (A–Z)",
                "TIME (Earliest to Latest)",
                "WEEKDAYS",
                "SATURDAY",
                "SUNDAY"
        );
        programChoiceBox.setValue("TITLE (A–Z)");

        programChoiceBox.setStyle("""
        -fx-background-color: #535252;
        -fx-mark-color: white;
        -fx-font-weight: bold;
        -fx-text-fill: white;
    """);

        loadPrograms("TITLE (A–Z)");

        programChoiceBox.setOnAction(e ->
                loadPrograms(programChoiceBox.getValue()));
    }

    private void loadPrograms(String filter) {
        programList.getChildren().clear();

        List<Program> programs = ProgramDAO.getProgramsWithDaysAndHosts();

        switch (filter) {
            case "TIME (Earliest to Latest)" ->
                    programs.sort(Comparator.comparing(p ->
                            LocalTime.parse(p.getStartTime())
                    ));

            case "WEEKDAYS" ->
                    programs.removeIf(p -> p.getDays() == null || !p.getDays().contains("WEEKDAYS"));

            case "SATURDAY" ->
                    programs.removeIf(p -> p.getDays() == null || !p.getDays().contains("SAT"));

            case "SUNDAY" ->
                    programs.removeIf(p -> p.getDays() == null || !p.getDays().contains("SUN"));

            default ->
                    programs.sort(Comparator.comparing(p -> p.getTitle()));
        }

        programs.forEach(p ->
                programList.getChildren().add(createProgramCard(p)));
    }

    private AnchorPane createProgramCard(Program p) {

        AnchorPane card = new AnchorPane();
        card.setStyle("""
            -fx-background-color: #CFCECE;
            -fx-background-radius: 20;
            -fx-padding: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.2, 0, 3);
        """);
        card.setPrefHeight(80);

        HBox root = new HBox(25);
        root.setPrefWidth(1200);

        VBox left = new VBox(3);
        left.setPrefWidth(460);

        // TITLE + STATUS (inline)
        HBox titleStatus = new HBox(10);
        titleStatus.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        VBox.setMargin(titleStatus, new javafx.geometry.Insets(20, 0, 0, 20));

        Text title = new Text(p.getTitle());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900;");

        Label status = new Label(isOnAir(p) ? "ON AIR" : "OFFLINE");
        status.setStyle(isOnAir(p)
                ? "-fx-background-color: #FFEC40; -fx-text-fill: black;"
                : "-fx-background-color: #9A2020; -fx-text-fill: white;"
        );
        status.setStyle(status.getStyle() +
                "-fx-padding: 4 10; -fx-background-radius: 12; -fx-font-weight: 700;");

        titleStatus.getChildren().addAll(title, status);

        Text schedule = new Text(
                formatTime(p.getStartTime()) + " – " +
                        formatTime(p.getEndTime()) + " | " +
                        (p.getDays() != null ? p.getDays() : "")
        );
        schedule.setStyle("-fx-font-weight: 500;");
        VBox.setMargin(schedule, new javafx.geometry.Insets(3, 0, 0, 20));

        Text hosts = new Text(
                (p.getHosts() != null && !p.getHosts().isEmpty())
                        ? p.getHosts()
                        : p.getType()
        );
        hosts.setStyle("-fx-fill: #4a4a4a; -fx-font-size: 14px;");
        VBox.setMargin(hosts, new javafx.geometry.Insets(0, 0, 0, 20));

        left.getChildren().addAll(titleStatus, schedule, hosts);

        VBox right = new VBox();
        right.setPrefWidth(700);

        Text desc = new Text(
                p.getDescription() != null ? p.getDescription() : "Program description here"
        );
        desc.setWrappingWidth(680);
        desc.setStyle("-fx-font-size: 14.5px; -fx-fill: #333;");
        VBox.setMargin(desc,new javafx.geometry.Insets(22, 0, 0, 60));

        right.getChildren().add(desc);

        root.getChildren().addAll(left, right);
        card.getChildren().add(root);

        return card;
    }

    private boolean isOnAir(Program p) {
        if (p.getDays() == null || p.getStartTime() == null || p.getEndTime() == null) return false;

        LocalTime now = LocalTime.now();

        LocalTime start = LocalTime.parse(p.getStartTime());
        LocalTime end   = LocalTime.parse(p.getEndTime());

        DayOfWeek today = LocalDate.now().getDayOfWeek();

        boolean dayMatch =
                (p.getDays().contains("WEEKDAYS") &&
                        today != DayOfWeek.SATURDAY &&
                        today != DayOfWeek.SUNDAY)
                        || (p.getDays().contains("SAT") && today == DayOfWeek.SATURDAY)
                        || (p.getDays().contains("SUN") && today == DayOfWeek.SUNDAY);

        // handle overnight programs (e.g. 10 PM – 2 AM)
        if (end.isBefore(start)) {
            return dayMatch && (now.isAfter(start) || now.isBefore(end));
        }
        return dayMatch && !now.isBefore(start) && !now.isAfter(end);
    }

    private String formatTime(String time24) {
        return LocalTime.parse(time24)
                .format(DateTimeFormatter.ofPattern("h:mm a")).toUpperCase();
    }
}
