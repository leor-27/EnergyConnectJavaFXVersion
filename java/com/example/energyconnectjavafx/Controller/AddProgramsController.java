package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.ProgramDAO;
import com.example.energyconnectjavafx.Model.Program;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class AddProgramsController extends NavController {
    @FXML
    private ComboBox<String> programType;

    @FXML
    private VBox programListContainer;
    @FXML private TextField titleField;
    @FXML private TextField descArea;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;

    @FXML private CheckBox weekdaysCB;
    @FXML private CheckBox satCB;
    @FXML private CheckBox sunCB;

    @FXML private CheckBox arnelCB;
    @FXML private CheckBox appleCB;
    @FXML private CheckBox barbieCB;
    @FXML private CheckBox makisigCB;
    @FXML private CheckBox bokCB;
    @FXML private CheckBox gatsCB;

    private boolean editMode = false;
    private int editingProgramId;

    @FXML
    private Button addProgramBtn;

    public void initialize() {
        programType.getItems().addAll(
                "MUSIC ONLY",
                "WITH DJ/HOST"
        );
        loadPrograms();
        programType.valueProperty().addListener((obs, oldVal, newVal) -> toggleDJFields());
        toggleDJFields();
    }

    private void loadPrograms() {
        programListContainer.getChildren().clear();

        List<Program> programs = ProgramDAO.getProgramsWithDaysAndHosts();

        programs.sort((a, b) ->
                a.getTitle().compareToIgnoreCase(b.getTitle())
        );

        for (Program p : programs) {
            programListContainer.getChildren().add(createProgramCard(p));
        }
    }

    private Node createProgramCard(Program p) {
        AnchorPane card = new AnchorPane();
        card.setPrefHeight(125);
        card.setPrefWidth(1368);
        card.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-background-radius: 5;
            -fx-border-color: #ddd;
            -fx-border-radius: 5;
        """);

        Text title = new Text(p.getTitle());
        title.setLayoutX(22);
        title.setLayoutY(32);
        title.setWrappingWidth(280);
        title.setStyle("-fx-font-size: 19px; -fx-font-weight: bold;");

        Text schedule = new Text(
                formatTime(p.getStartTime()) + " – " + formatTime(p.getEndTime())
        );
        schedule.setLayoutX(22);
        schedule.setLayoutY(72);
        schedule.setStyle("-fx-font-size: 15px;");

        Text desc = new Text(p.getDescription());
        desc.setLayoutX(330);
        desc.setLayoutY(35);
        desc.setWrappingWidth(900);
        desc.setStyle("-fx-fill: #666;");

        String hosts = (p.getHosts() == null || p.getHosts().isBlank())
                ? "MUSIC ONLY"
                : p.getHosts().toUpperCase();;

        Text hostsText = new Text(hosts);
        hostsText.setLayoutX(22);
        hostsText.setLayoutY(90);
        hostsText.setStyle("-fx-fill: #666;");

        Text daysText = new Text(formatDays(p.getDays()));
        daysText.setLayoutX(22);
        daysText.setLayoutY(108);
        daysText.setStyle("-fx-fill: #666;");

        ImageView editIcon = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/energyconnectjavafx/Images/edit.png"))
        );
        editIcon.setFitWidth(18);
        editIcon.setFitHeight(18);
        editIcon.setLayoutX(1270);
        editIcon.setLayoutY(22);
        editIcon.setStyle("-fx-cursor: hand;");

        editIcon.setOnMouseClicked(e -> loadForEdit(p.getProgramId()));

        ImageView deleteIcon = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/energyconnectjavafx/Images/delete.png"))
        );
        deleteIcon.setFitWidth(15);
        deleteIcon.setFitHeight(18);
        deleteIcon.setLayoutX(1302);
        deleteIcon.setLayoutY(22);
        deleteIcon.setStyle("-fx-cursor: hand;");

        deleteIcon.setOnMouseClicked(e ->
                confirmDeleteProgram(p.getProgramId())
        );

        card.getChildren().addAll(
                title, schedule, desc, daysText, hostsText,
                editIcon, deleteIcon
        );

        return card;
    }

    private void goToAddProgramsEdit(int programId) {
        navigate("/com/example/energyconnectjavafx/addPrograms-view.fxml?edit=" + programId);
    }

    private void confirmDeleteProgram(int programId) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Program");
        alert.setContentText("Are you sure you want to delete this program?");

        // 🔑 IMPORTANT LINE
        alert.initOwner(programListContainer.getScene().getWindow());

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                ProgramDAO.deleteProgram(programId);
                loadPrograms();
            }
        });
    }

    @FXML
    private void handleAddProgram() {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            showError("Title is required");
            return;
        }

        if (programType.getValue() == null) {
            showError("Program type is required.");
            return;
        }

        Program p = new Program(
                0,
                titleField.getText().trim().toUpperCase(),
                programType.getValue(),
                null,
                null,
                descArea.getText(),
                1,
                null,
                null
        );

        try {
            p.setStartTime(parseToDbTime(startTimeField.getText()));
            p.setEndTime(parseToDbTime(endTimeField.getText()));
        } catch (IllegalArgumentException ex) {
            showError("Invalid time format. Use 12:30 AM / PM");
            return;
        }

        List<String> days = new ArrayList<>();
        if (weekdaysCB.isSelected()) days.add("1");
        if (satCB.isSelected()) days.add("2");
        if (sunCB.isSelected()) days.add("3");

        if (days.isEmpty()) {
            showError("Please select at least one schedule day.");
            return;
        }

        List<String> hosts = new ArrayList<>();

        if ("WITH DJ/HOST".equals(p.getType())) {
            if (arnelCB.isSelected()) hosts.add("ARNEL ECLARINAL");
            if (appleCB.isSelected()) hosts.add("DJ APPLE");
            if (barbieCB.isSelected()) hosts.add("DJ BARBIE");
            if (makisigCB.isSelected()) hosts.add("DJ MAKISIG");
            if (bokCB.isSelected()) hosts.add("KUYA BOK");
            if (gatsCB.isSelected()) hosts.add("PAPA GATS");

            if (hosts.isEmpty()) {
                showError("Please select at least one DJ/Host.");
                return;
            }
        }

        try {
            boolean wasEdit = editMode;

            if (editMode) {
                ProgramDAO.updateProgram(editingProgramId, p, days, hosts);
            } else {
                ProgramDAO.addProgram(p, days, hosts);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(wasEdit
                    ? "Program successfully updated."
                    : "Program successfully added.");
            alert.initOwner(programListContainer.getScene().getWindow());
            alert.showAndWait();

            resetForm();
            loadPrograms();

        } catch (Exception e) {
            showError("Failed to save program.");
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);

        a.initOwner(addProgramBtn.getScene().getWindow());

        a.showAndWait();
    }

    private void clearForm() {
        titleField.clear();
        descArea.clear();
        startTimeField.clear();
        endTimeField.clear();
        programType.getSelectionModel().clearSelection();

        weekdaysCB.setSelected(false);
        satCB.setSelected(false);
        sunCB.setSelected(false);

        arnelCB.setSelected(false);
        appleCB.setSelected(false);
        barbieCB.setSelected(false);
        makisigCB.setSelected(false);
        bokCB.setSelected(false);
        gatsCB.setSelected(false);
    }

    private void toggleDJFields() {
        boolean withDJ = "WITH DJ/HOST".equals(programType.getValue());

        arnelCB.setDisable(!withDJ);
        appleCB.setDisable(!withDJ);
        barbieCB.setDisable(!withDJ);
        makisigCB.setDisable(!withDJ);
        bokCB.setDisable(!withDJ);
        gatsCB.setDisable(!withDJ);

        if (!withDJ) {
            arnelCB.setSelected(false);
            appleCB.setSelected(false);
            barbieCB.setSelected(false);
            makisigCB.setSelected(false);
            bokCB.setSelected(false);
            gatsCB.setSelected(false);
        }
    }

    private String formatTime(String time) {
        try {
            java.time.LocalTime t = java.time.LocalTime.parse(time);
            return t.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")).toUpperCase();
        } catch (Exception e) {
            return time; // fallback (safety)
        }
    }

    private String formatDays(String days) {
        if (days == null || days.isBlank()) return "";

        List<String> list = new ArrayList<>();
        for (String d : days.split(",")) {
            list.add(d.trim());
        }

        boolean hasWeekdays = list.contains("WEEKDAYS");
        boolean hasSat = list.contains("SAT");
        boolean hasSun = list.contains("SUN");

        // ONLY weekdays
        if (hasWeekdays && !hasSat && !hasSun) {
            return "WEEKDAYS";
        }

        // Combination cases
        List<String> display = new ArrayList<>();
        if (hasWeekdays) display.add("WEEKDAYS");
        if (hasSat) display.add("SAT");
        if (hasSun) display.add("SUN");

        return String.join(", ", display);
    }

    private String parseToDbTime(String input) {
        if (input == null) throw new IllegalArgumentException();

        // Normalize EVERYTHING
        String s = input
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim()
                .toUpperCase();

        // Accept: 05:55 AM, 5:55AM, 05:55AM, etc
        java.util.regex.Pattern p =
                java.util.regex.Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(AM|PM)");

        java.util.regex.Matcher m = p.matcher(s);
        if (!m.matches()) {
            throw new IllegalArgumentException();
        }

        int hour = Integer.parseInt(m.group(1));
        int minute = Integer.parseInt(m.group(2));
        String meridian = m.group(3);

        if (hour < 1 || hour > 12 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException();
        }

        if ("PM".equals(meridian) && hour != 12) hour += 12;
        if ("AM".equals(meridian) && hour == 12) hour = 0;

        return String.format("%02d:%02d:00", hour, minute);
    }

    private void loadForEdit(int programId) {
        editMode = true;
        editingProgramId = programId;

        Program p = ProgramDAO.getProgramById(programId);
        if (p == null) {
            showError("Program not found.");
            return;
        }

        // Change button text
        addProgramBtn.setText("Update");

        // Fill fields
        titleField.setText(p.getTitle());
        descArea.setText(p.getDescription());
        startTimeField.setText(formatTime(p.getStartTime()));
        endTimeField.setText(formatTime(p.getEndTime()));
        programType.setValue(p.getType());
        toggleDJFields();

        // Days
        String days = p.getDays() == null ? "" : p.getDays().toUpperCase();
        weekdaysCB.setSelected(days.contains("WEEKDAYS"));
        satCB.setSelected(days.contains("SAT"));
        sunCB.setSelected(days.contains("SUN"));

        // Hosts
        clearHostCheckboxes();
        if (p.getHosts() != null) {
            if (p.getHosts().contains("ARNEL")) arnelCB.setSelected(true);
            if (p.getHosts().contains("APPLE")) appleCB.setSelected(true);
            if (p.getHosts().contains("BARBIE")) barbieCB.setSelected(true);
            if (p.getHosts().contains("MAKISIG")) makisigCB.setSelected(true);
            if (p.getHosts().contains("BOK")) bokCB.setSelected(true);
            if (p.getHosts().contains("GATS")) gatsCB.setSelected(true);
        }
    }

    private void resetForm() {
        editMode = false;
        editingProgramId = 0;
        addProgramBtn.setText("Add");
        clearForm();
    }

    private void clearHostCheckboxes() {
        arnelCB.setSelected(false);
        appleCB.setSelected(false);
        barbieCB.setSelected(false);
        makisigCB.setSelected(false);
        bokCB.setSelected(false);
        gatsCB.setSelected(false);
    }
}
