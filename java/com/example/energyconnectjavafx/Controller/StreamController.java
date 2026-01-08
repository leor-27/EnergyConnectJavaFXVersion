package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.ProgramDAO;
import com.example.energyconnectjavafx.DAO.StreamDAO;
import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.Program;
import com.example.energyconnectjavafx.Model.StreamItem;
import com.example.energyconnectjavafx.util.WebContentGenerator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.shape.Rectangle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import java.awt.Desktop;
import java.net.URI;

public class StreamController extends NavController {

    private static final File WEB_MEDIA_ROOT = new File("/Applications/XAMPP/htdocs/EnergyConnect");
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    private MediaPlayer currentPlayer;
    private FilteredList<StreamItem> filteredData;
    private StreamItem currentItem;

    @FXML
    private TableView<StreamItem> broadcastTable;
    @FXML
    private TableColumn<StreamItem, String> colBroadName;
    @FXML
    private TableColumn<StreamItem, String> colBroadDate;
    @FXML
    private TableColumn<StreamItem, String> colBroadTime;
    @FXML
    private TableColumn<StreamItem, Void> colBroadAction;
    @FXML
    private StackPane broadcastContainer;

    @FXML
    private WebView facebookWebView;
    @FXML
    private WebView youtubeWebView;

    @FXML
    private TextField searchInput;
    @FXML
    private ComboBox<String> programFilter;

    @FXML
    public void initialize() {
        try {
            // 1. Setup Audio Table
            ObservableList<StreamItem> broadcastData = StreamDAO.fetchAll(DBConnection.getConnection());
            filteredData = new FilteredList<>(broadcastData, p -> true);
            setupTable(broadcastTable, colBroadName, colBroadDate, colBroadTime, colBroadAction, filteredData, false);
            colBroadName.setStyle("-fx-alignment: CENTER-LEFT;");
            clipChildren(broadcastContainer, 12);

            // 2. Setup Search
            populateProgramFilter();
            if (searchInput != null) searchInput.textProperty().addListener((obs, old, newVal) -> filterTable());
            if (programFilter != null) programFilter.valueProperty().addListener((obs, old, newVal) -> filterTable());

            // 3. Load Web Content
            loadWebContent();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadWebContent() {
        if (facebookWebView != null) {
            WebEngine fbEngine = facebookWebView.getEngine();
            fbEngine.setUserAgent(USER_AGENT);
            fbEngine.load(WebContentGenerator.getFacebookUrl());

            fbEngine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
                if (newLoc != null && !newLoc.equals(WebContentGenerator.getFacebookUrl())) {
                    Platform.runLater(() -> fbEngine.load(WebContentGenerator.getFacebookUrl())); // Reset
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI(newLoc));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (youtubeWebView != null) {
            try {
                String htmlContent = WebContentGenerator.getYouTubeCarouselHtml("UCJRPf-4NvEbTGY-zYWcOqwg");
                File tempFile = File.createTempFile("energy_youtube", ".html");
                tempFile.deleteOnExit();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    writer.write(htmlContent);
                }

                WebEngine ytEngine = youtubeWebView.getEngine();
                ytEngine.setJavaScriptEnabled(true);
                ytEngine.setUserAgent(USER_AGENT);
                ytEngine.load(tempFile.toURI().toString());
                youtubeWebView.setContextMenuEnabled(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateProgramFilter() {
        if (programFilter == null) return;
        List<Program> allPrograms = ProgramDAO.getPrograms();
        List<String> titles = allPrograms.stream().map(Program::getTitle).distinct().sorted().collect(Collectors.toList());
        ObservableList<String> options = FXCollections.observableArrayList(titles);
        options.add(0, "All Programs");
        programFilter.setItems(options);
        programFilter.getSelectionModel().selectFirst();
    }

    private void filterTable() {
        String searchText = (searchInput.getText() == null) ? "" : searchInput.getText().toLowerCase();
        String selectedProgram = programFilter.getValue();

        filteredData.setPredicate(item -> {
            boolean matchesSearch = item.getTitle().toLowerCase().contains(searchText) || item.getDate().toLowerCase().contains(searchText);
            boolean matchesProgram = true;
            if (selectedProgram != null && !selectedProgram.equals("All Programs")) {
                matchesProgram = item.getTitle().toLowerCase().contains(selectedProgram.toLowerCase());
            }
            return matchesSearch && matchesProgram;
        });
    }

    private void setupTable(TableView<StreamItem> table, TableColumn<StreamItem, String> nameCol, TableColumn<StreamItem, String> dateCol, TableColumn<StreamItem, String> timeCol, TableColumn<StreamItem, Void> actionCol, ObservableList<StreamItem> data, boolean isDarkTheme) {
        FilteredList<StreamItem> filtered =
                (data instanceof FilteredList)
                        ? (FilteredList<StreamItem>) data
                        : new FilteredList<>(data);

        SortedList<StreamItem> sortedData = new SortedList<>(filtered);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTitle()));

        dateCol.setCellValueFactory(cell -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(LocalDate.parse(cell.getValue().getDate()).format(dateFormatter));
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty(cell.getValue().getDate());
            }
        });

        timeCol.setCellValueFactory(cell -> {
            try {
                String[] parts = cell.getValue().getTime().split(" - ");
                return new javafx.beans.property.SimpleStringProperty(LocalTime.parse(parts[0]).format(timeFormatter) + " - " + LocalTime.parse(parts[1]).format(timeFormatter));
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty(cell.getValue().getTime());
            }
        });
        table.setItems(sortedData);

        table.getSortOrder().add(dateCol);
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        table.sort();

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button playBtn = new Button("▶");
            private final HBox pane = new HBox(15, playBtn);

            {
                actionCol.setCellFactory(col -> new TableCell<>() {

                    private final Button playBtn = new Button();

                    {
                        playBtn.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-text-fill: #555;" +
                                        "-fx-font-size: 26px;" +
                                        "-fx-padding: 0;"
                        );
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getIndex() < 0) {
                            setGraphic(null);
                            return;
                        }

                        StreamItem streamItem = getTableView().getItems().get(getIndex());

                        // VERY IMPORTANT: unbind before rebind
                        playBtn.textProperty().unbind();

                        playBtn.textProperty().bind(
                                javafx.beans.binding.Bindings.when(streamItem.playingProperty())
                                        .then("❚❚")
                                        .otherwise("▶")
                        );

                        playBtn.setOnAction(e -> playAudio(streamItem));

                        setGraphic(playBtn);
                    }
                });
                playBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: #555; -fx-font-size: 26px; -fx-padding: 0;");
                pane.setStyle("-fx-alignment: CENTER_RIGHT; -fx-padding: 0 10 0 0;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void clipChildren(Region region, double arc) {
        Rectangle outputClip = new Rectangle();
        outputClip.setArcWidth(arc * 2);
        outputClip.setArcHeight(arc * 2);
        outputClip.widthProperty().bind(region.widthProperty());
        outputClip.heightProperty().bind(region.heightProperty());
        region.setClip(outputClip);
    }

    private void playAudio(StreamItem item) {

        if (item.getAudioPath() == null || item.getAudioPath().isBlank()) return;

        try {
            // Toggle same item
            if (currentItem == item) {
                stopCurrent();
                return;
            }

            // Stop previous
            stopCurrent();

            File audioFile = new File(WEB_MEDIA_ROOT, item.getAudioPath());
            if (!audioFile.exists()) {
                System.out.println("Audio missing: " + audioFile.getAbsolutePath());
                return;
            }

            Media media = new Media(audioFile.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);

            currentPlayer = player;
            currentItem = item;

            item.setPlaying(true);
            player.play();

            player.setOnEndOfMedia(this::stopCurrent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopCurrent() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
        }

        if (currentItem != null) {
            currentItem.setPlaying(false);
        }

        currentPlayer = null;
        currentItem = null;
    }
}