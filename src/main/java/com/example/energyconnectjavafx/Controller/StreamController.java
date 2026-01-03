package com.example.energyconnectjavafx.Controller;

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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class StreamController extends NavController {

    @FXML private TableView<StreamItem> broadcastTable;
    @FXML private TableColumn<StreamItem, String> colBroadName;
    @FXML private TableColumn<StreamItem, String> colBroadDate;
    @FXML private TableColumn<StreamItem, String> colBroadTime;
    @FXML private TableColumn<StreamItem, Void> colBroadAction;
    @FXML private StackPane broadcastContainer;

    @FXML private ScrollPane carouselScroll;

    @FXML
    public void initialize() {

        //EDIT DATA HERE
        ObservableList<StreamItem> broadcastData = FXCollections.observableArrayList(
                new StreamItem("Audio Broadcast #1", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #2", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #3", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #4", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #5", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #6", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #7", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #8", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #9", "Date Played", "Start - End Time"),
                new StreamItem("Audio Broadcast #10","Date Played", "Start - End Time")
        );

        setupTable(broadcastTable, colBroadName, colBroadDate, colBroadTime, colBroadAction, broadcastData, false);

        colBroadName.setStyle("-fx-alignment: CENTER-LEFT;");

        clipChildren(broadcastContainer, 12);
    }

    private void setupTable(TableView<StreamItem> table,
                            TableColumn<StreamItem, String> nameCol,
                            TableColumn<StreamItem, String> dateCol,
                            TableColumn<StreamItem, String> timeCol,
                            TableColumn<StreamItem, Void> actionCol,
                            ObservableList<StreamItem> data,
                            boolean isDarkTheme) {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        table.setItems(data);

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button playBtn = new Button("▶");
            private final Button menuBtn = new Button("⋮");
            private final HBox pane = new HBox(15, playBtn);

            {
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

    //CAROUSEL LOGIC

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

    public static class StreamItem {
        private String name;
        private String date;
        private String time;

        public StreamItem(String name, String date, String time) {
            this.name = name;
            this.date = date;
            this.time = time;
        }

        public String getName() { return name; }
        public String getDate() { return date; }
        public String getTime() { return time; }
    }
}