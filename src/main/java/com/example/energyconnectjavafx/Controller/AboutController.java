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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController extends NavController implements Initializable {

    @FXML private MenuButton menuButton;
    @FXML private ImageView logoHomepage;
    @FXML private WebView mapView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("About Page Controller initialized!");

        Platform.runLater(() -> {
            hideMenuButtonArrow();
            wireMenuNavigation();   // 🔑 THIS was missing
        });
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

    // ✅ Wire MenuItems without ActionEvent methods
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
}