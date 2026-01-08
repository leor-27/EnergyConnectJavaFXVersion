package com.example.energyconnectjavafx.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController extends NavController implements Initializable {

    @FXML private MenuButton menuButton;
    @FXML private ImageView logoHomepage;
    @FXML private WebView mapView;

    @FXML private Hyperlink facebookPageLink;
    @FXML private Hyperlink facebookDJMakisigLink;
    @FXML private Hyperlink tiktokLink;
    @FXML private Hyperlink youtubeLink;
    @FXML private Hyperlink emailLink;
    @FXML private Hyperlink phoneLink;
    @FXML private Hyperlink locationLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("About Page Controller initialized!");

        Platform.runLater(() -> {
            hideMenuButtonArrow();
            wireMenuNavigation();
        });

        loadEnergyFMMap();
    }

    protected void switchScene(Node anyNodeInScene, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/energyconnectjavafx/View/" + fxmlFile)
            );
            Parent newRoot = loader.load();

            Stage stage = (Stage) anyNodeInScene.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(newRoot);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void wireMenuNavigation() {
        for (MenuItem item : menuButton.getItems()) {
            item.setOnAction(e -> {
                switch (item.getText()) {
                    case "About" -> switchScene(menuButton, "about-view.fxml");
                    case "Profiles" -> switchScene(menuButton, "profileMain-view.fxml");
                    case "Programs" -> switchScene(menuButton, "program-view.fxml");
                    case "Stream" -> switchScene(menuButton, "stream-view.fxml");
                    case "News" -> switchScene(menuButton, "news-view.fxml");
                }
            });
        }
    }

    private void loadEnergyFMMap() {
        if (mapView == null) {
            System.out.println("mapView is null!");
            return;
        }

        try {
            System.out.println("Loading map...");
            mapView.getEngine().loadContent(createMapHTML());
            configureWebView();
            System.out.println("EnergyFM Naga map loaded successfully!");

        } catch (Exception e) {
            System.out.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureWebView() {
        try {
            mapView.setContextMenuEnabled(false);
            mapView.getEngine().setJavaScriptEnabled(true);
            mapView.setOnMouseClicked(e ->
                    openWebPage("https://maps.app.goo.gl/XydzqMZs4kV1EdJe7")
            );
        } catch (Exception e) {
            System.out.println("Error configuring WebView: " + e.getMessage());
        }
    }

    private void hideMenuButtonArrow() {
        try {
            Node arrow = menuButton.lookup(".arrow-button");
            if (arrow != null) {
                arrow.setVisible(false);
                arrow.setManaged(false);
            }
        } catch (Exception e) {
            System.out.println("Could not hide arrow: " + e.getMessage());
        }
    }

    @FXML private void openFacebookPage(ActionEvent e) {
        openWebPage("https://www.facebook.com/share/1D8mB58KNW/?mibextid=wwXIfr");
    }

    @FXML private void openFacebookDJMakisig(ActionEvent e) {
        openWebPage("https://www.facebook.com/share/1bGKXAipkL/?mibextid=wwXIfr");
    }

    @FXML private void openTikTok(ActionEvent e) {
        openWebPage("https://www.tiktok.com/@djmakisig");
    }

    @FXML private void openYouTube(ActionEvent e) {
        openWebPage("https://youtube.com/@djmakisig?si=i3tHyXQdtRPaq-Jj");
    }

    @FXML private void openEmail(ActionEvent e) {
        openWebPage("https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&to=energyfmnaga1063@gmail.com");
    }

    @FXML private void dialPhone(ActionEvent e) {
        openWebPage("tel:+639171137249");
    }

    @FXML private void openLocation(ActionEvent e) {
        openWebPage("https://maps.app.goo.gl/XydzqMZs4kV1EdJe7");
    }

    private void openWebPage(String url) {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert(
                    "Cannot Open The Link",
                    "Please copy and paste this URL into your browser:\n\n" + url
            );
        }
    }

    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String createMapHTML() {
        return """
        <!DOCTYPE html>
        <html>
            <head>
                <style>
                    body, html {
                        margin: 0;
                        padding: 0;
                        width: 100%;
                        height: 100%;
                        overflow: hidden;
                    }
                    .map-container {
                        width: 100%;
                        height: 100%;
                    }
                    iframe {
                        width: 100%;
                        height: 100%;
                        border: 0;
                    }
                </style>
            </head>
            <body>
                <div class="map-container">
                    <iframe 
                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d5457.849416366149!2d123.18269377631093!3d13.624453800226762!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x33a18cb151b657bb%3A0x4a9f7a67e5199747!2sTraders%20Square%2C%20Naga%20City%2C%204400%20Camarines%20Sur!5e1!3m2!1sen!2sph!4v1767458804985!5m2!1sen!2sph"
                        allowfullscreen=""
                        loading="lazy"
                        referrerpolicy="no-referrer-when-downgrade">
                    </iframe>
                </div>
            </body>
        </html>
        """;
    }
}
