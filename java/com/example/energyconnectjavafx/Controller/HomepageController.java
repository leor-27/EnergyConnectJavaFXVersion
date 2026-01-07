package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.NewsDAO;
import com.example.energyconnectjavafx.DAO.ProgramDAO;
import com.example.energyconnectjavafx.HelloApplication;
import com.example.energyconnectjavafx.Model.News;
import com.example.energyconnectjavafx.Model.Program;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;

public class HomepageController extends NavController {

    private MediaPlayer player;
    private boolean playing = false;

    private Image playIcon;
    private Image pauseIcon;

    @FXML private HBox newsContainer;
    @FXML private Label onAirLabel;
    @FXML private Label liveButtonLabel;
    @FXML private ImageView liveButtonIcon;
    @FXML private HBox featuredProgramsBox;

    public void initialize() {

        URL playUrl = getClass().getResource("/com/example/energyconnectjavafx/Images/live_player.png");
        URL pauseUrl = getClass().getResource("/com/example/energyconnectjavafx/Images/pause_button.png");

        if (playUrl == null || pauseUrl == null) {
            throw new RuntimeException("Live player icons not found in /Images/");
        }

        playIcon = new Image(playUrl.toExternalForm());
        pauseIcon = new Image(pauseUrl.toExternalForm());

        Media media = new Media("https://stream.zeno.fm/klewk28qlqquv.m3u");
        player = new MediaPlayer(media);

        setPlayState();
        loadOnAirProgram();
        loadFeaturedPrograms();
        loadFeaturedNews();
    }

    @FXML
    private void toggleLive() {
        if (!playing) {
            player.play();
            playing = true;
            setPauseState();
        } else {
            player.pause();
            playing = false;
            setPlayState();
        }
    }

    private void loadOnAirProgram() {
        List<Program> programs = ProgramDAO.getPrograms();
        LocalTime now = LocalTime.now();
        Program onAir = null;

        for (Program p : programs) {
            LocalTime start = LocalTime.parse(p.getStartTime().substring(0, 5));
            LocalTime end   = LocalTime.parse(p.getEndTime().substring(0, 5));

            boolean active = start.isBefore(end)
                    ? !now.isBefore(start) && !now.isAfter(end)
                    : !now.isBefore(start) || !now.isAfter(end);

            if (active) {
                onAir = p;
                break;
            }
        }

        onAirLabel.setText(
                onAir != null ? "On Air: " + onAir.getTitle() : "No Active Program"
        );
    }

    private void loadFeaturedPrograms() {
        List<Program> programs = ProgramDAO.getFeaturedProgramsWithHosts();
        featuredProgramsBox.getChildren().clear();

        for (Program p : programs) {

            VBox card = new VBox(8);
            card.setPrefSize(450, 120);
            card.setStyle("""
                -fx-background-color: rgba(237,239,242,1);
                -fx-background-radius: 20;
                -fx-padding: 15;
                 -fx-cursor: hand;
            """);
            card.setEffect(new DropShadow());
            card.setCursor(javafx.scene.Cursor.HAND);
            card.setOnMouseClicked(e -> goToPrograms(card));
            card.setOnMouseEntered(e -> card.setOpacity(0.9));
            card.setOnMouseExited(e -> card.setOpacity(1));

            HBox header = new HBox(15);
            ImageView mic = new ImageView(
                    new Image(getClass().getResource("/com/example/energyconnectjavafx/Images/mic_player.png").toExternalForm())
            );
            mic.setFitHeight(30);
            mic.setPreserveRatio(true);

            Label title = new Label(p.getTitle() + " | " + p.getDays());
            title.setStyle("-fx-font-weight: 700; -fx-font-size: 20");

            header.getChildren().addAll(mic, title);

            Label time = new Label("Time: " + p.getStartTime() + " - " + p.getEndTime());
            time.setStyle("-fx-font-size: 18");

            Label hosts = new Label("Hosts: " + p.getHosts());
            hosts.setStyle("-fx-font-size: 18");

            card.getChildren().addAll(header, time, hosts);
            featuredProgramsBox.getChildren().add(card);
        }
    }

    private void setPlayState() {
        liveButtonLabel.setText("LISTEN LIVE HERE");
        liveButtonIcon.setImage(playIcon);
    }

    private void setPauseState() {
        liveButtonLabel.setText("PAUSE LIVE STREAM");
        liveButtonIcon.setImage(pauseIcon);
    }

    private void loadFeaturedNews() {
        List<News> newsList;

        try {
            newsList = new NewsDAO().getLatestNews();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        newsContainer.getChildren().clear();

        for (News n : newsList) {

            VBox card = new VBox(10);
            card.setPrefWidth(370);
            card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 20;
            -fx-padding: 15;
        """);
            card.setEffect(new DropShadow());
            card.setOnMouseEntered(e -> card.setOpacity(0.9));
            card.setOnMouseExited(e -> card.setOpacity(1));

            ImageView img = createCoverImage(n.getImagePath(), 325, 300);
            img.setCursor(javafx.scene.Cursor.HAND);
            img.setOnMouseClicked(e -> {
                if (n.getSourceUrl() != null && !n.getSourceUrl().isBlank()) {
                    HelloApplication.getHostServicesInstance().showDocument(n.getSourceUrl());
                }
            });
            img.setStyle("-fx-background-radius: 10;");
            img.setSmooth(true);

            Label headline = new Label(n.getHeadline());
            headline.setWrapText(true);
            headline.setStyle("-fx-font-size: 20; -fx-font-weight: 700;");

            Label meta = new Label(
                    n.getOrganization() + " | " + formatNewsDate(n.getDatePosted())
            );
            meta.setStyle("-fx-font-size: 14; -fx-text-fill: #555;");

            Label author = new Label("By " + n.getAuthor());
            author.setStyle("-fx-font-size: 14;");

            Label summary = new Label(n.getSummary());
            summary.setWrapText(true);
            summary.setStyle("-fx-font-size: 15;");
            summary.setMaxHeight(60);
            summary.setTextOverrun(OverrunStyle.ELLIPSIS);

            card.getChildren().addAll(img, headline, meta, author, summary);
            newsContainer.getChildren().add(card);
        }
    }

    private ImageView createCoverImage(String path, double width, double height) {
        Image image = new Image(
                path.startsWith("http") ? path : "file:" + path,
                false
        );

        ImageView view = new ImageView(image);
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setPreserveRatio(true);

        // crop center (object-fit: cover)
        double imgWidth = image.getWidth();
        double imgHeight = image.getHeight();

        double scale = Math.max(width / imgWidth, height / imgHeight);
        double scaledW = imgWidth * scale;
        double scaledH = imgHeight * scale;

        double x = (scaledW - width) / 2 / scale;
        double y = (scaledH - height) / 2 / scale;

        view.setViewport(new javafx.geometry.Rectangle2D(
                x, y,
                imgWidth - 2 * x,
                imgHeight - 2 * y
        ));

        view.setSmooth(true);
        view.setCache(true);

        return view;
    }

    private String formatNewsDate(LocalDateTime posted) {
        LocalDateTime now = LocalDateTime.now();

        long seconds = Duration.between(posted, now).getSeconds();

        if (seconds < 60) return "Just now";

        long minutes = seconds / 60;
        if (minutes < 60)
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";

        long hours = minutes / 60;
        if (hours < 24)
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";

        return posted.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    }
}
