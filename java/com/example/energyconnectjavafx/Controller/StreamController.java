package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.StreamDAO;
import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.StreamItem;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.File;
import java.sql.SQLException;

public class StreamController extends NavController {

    private static final File WEB_MEDIA_ROOT =
            new File("/Applications/XAMPP/htdocs/EnergyConnect");

    private MediaPlayer currentPlayer;
    private Button currentButton;

    @FXML private TableView<StreamItem> broadcastTable;
    @FXML private TableColumn<StreamItem, String> colBroadName;
    @FXML private TableColumn<StreamItem, String> colBroadDate;
    @FXML private TableColumn<StreamItem, String> colBroadTime;
    @FXML private TableColumn<StreamItem, Void> colBroadAction;
    @FXML private StackPane broadcastContainer;

    @FXML private ScrollPane carouselScroll;

    @FXML
    public void initialize() {
        try {
            ObservableList<StreamItem> broadcastData =
                    StreamDAO.fetchAll(DBConnection.getConnection());

            setupTable(broadcastTable, colBroadName, colBroadDate,
                    colBroadTime, colBroadAction, broadcastData, false);

            colBroadName.setStyle("-fx-alignment: CENTER-LEFT;");
            clipChildren(broadcastContainer, 12);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTable(TableView<StreamItem> table,
                            TableColumn<StreamItem, String> nameCol,
                            TableColumn<StreamItem, String> dateCol,
                            TableColumn<StreamItem, String> timeCol,
                            TableColumn<StreamItem, Void> actionCol,
                            ObservableList<StreamItem> data,
                            boolean isDarkTheme) {

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("MMM dd, yyyy");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("h:mm a");

        nameCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getTitle()
                )
        );

        dateCol.setCellValueFactory(cell -> {
            try {
                LocalDate date = LocalDate.parse(cell.getValue().getDate());
                return new javafx.beans.property.SimpleStringProperty(
                        date.format(dateFormatter)
                );
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty(cell.getValue().getDate());
            }
        });

        timeCol.setCellValueFactory(cell -> {
            try {
                String[] parts = cell.getValue().getTime().split(" - ");
                LocalTime start = LocalTime.parse(parts[0]);
                LocalTime end = LocalTime.parse(parts[1]);

                return new javafx.beans.property.SimpleStringProperty(
                        start.format(timeFormatter) + " - " + end.format(timeFormatter)
                );
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty(cell.getValue().getTime());
            }
        });

        table.setItems(data);

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button playBtn = new Button("▶");
            private final Button menuBtn = new Button("⋮");
            private final HBox pane = new HBox(15, playBtn);

            {
                playBtn.setOnAction(e -> {
                    StreamItem item = getTableView().getItems().get(getIndex());
                    playAudio(item, playBtn);
                });

                String baseStyle = "-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0;";
                String textFill = isDarkTheme ? "-fx-text-fill: white;" : "-fx-text-fill: #555;";
                String hoverStyle = isDarkTheme ? "-fx-text-fill: #DDD;" : "-fx-text-fill: #000;";

                playBtn.setStyle(baseStyle + textFill + "-fx-font-size: 26px;");
                menuBtn.setStyle(baseStyle + textFill + "-fx-font-size: 24px;");

                playBtn.setOnMouseEntered(e -> playBtn.setStyle(baseStyle + hoverStyle + "-fx-font-size: 20px;"));
                playBtn.setOnMouseExited(e -> playBtn.setStyle(baseStyle + textFill + "-fx-font-size: 20px;"));

                if (isDarkTheme) {
                    pane.getChildren().add(menuBtn);
                    menuBtn.setOnMouseEntered(e -> menuBtn.setStyle(baseStyle + hoverStyle + "-fx-font-size: 20px;"));
                    menuBtn.setOnMouseExited(e -> menuBtn.setStyle(baseStyle + textFill + "-fx-font-size: 20px;"));
                }
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

    private final double SCROLL_AMOUNT = 0.35;

    @FXML
    private void scrollLeft() {
        if (carouselScroll != null) {
            double currentH = carouselScroll.getHvalue();
            double targetH = Math.max(0, currentH - SCROLL_AMOUNT);
            animateScroll(targetH);
        }
    }

    @FXML
    private void scrollRight() {
        if (carouselScroll != null) {
            double currentH = carouselScroll.getHvalue();
            double targetH = Math.min(1, currentH + SCROLL_AMOUNT);
            animateScroll(targetH);
        }
    }

    private void animateScroll(double targetHValue) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(carouselScroll.hvalueProperty(), targetHValue, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private void playAudio(StreamItem item, Button btn) {

        if (item.getAudioPath() == null || item.getAudioPath().isBlank()) {
            System.out.println("No audio file for this broadcast.");
            return;
        }

        try {
            // IF same button is pressed again → STOP
            if (currentPlayer != null && currentButton == btn) {
                currentPlayer.stop();
                currentPlayer.dispose();
                currentPlayer = null;
                currentButton = null;
                btn.setText("▶");
                return;
            }

            // STOP any other playing audio
            if (currentPlayer != null) {
                currentPlayer.stop();
                currentPlayer.dispose();
                currentButton.setText("▶");
            }

            File audioFile = new File(WEB_MEDIA_ROOT, item.getAudioPath());

            if (!audioFile.exists()) {
                System.out.println("Audio file not found: " + audioFile.getAbsolutePath());
                return;
            }

            Media media = new Media(audioFile.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);

            player.play();
            btn.setText("❚❚");

            currentPlayer = player;
            currentButton = btn;

            player.setOnEndOfMedia(() -> {
                btn.setText("▶");
                currentPlayer.dispose();
                currentPlayer = null;
                currentButton = null;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
