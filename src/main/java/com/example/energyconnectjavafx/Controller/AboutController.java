package com.example.energyconnectjavafx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML private ImageView menuButton;
    @FXML private ImageView logoHomepage;
    @FXML private WebView mapView;

    @Override public void initialize(URL location, ResourceBundle resources) {
        System.out.println("About Page Controller initialized!");

//        loadEnergyFMMap();

        Platform.runLater(() -> {
            hideMenuButtonArrow();
        });
    }
//    private void loadEnergyFMMap() {
//        if (mapView != null) {
//            try {
//                String energyFMMapUrl = "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d423.80992600129514!2d123.18524761621194!3d13.62420028960623!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x33a18cb1516eb475%3A0xc23076ed22c1014c!2sEnergy%20FM%20106.3%20Naga!5e1!3m2!1sen!2sph!4v1765472281618!5m2!1sen!2sph";
//                mapView.getEngine().load(energyFMMapUrl);
//
//                // Optional: Configure WebView settings
//                configureWebView();
//
//                System.out.println("EnergyFM Naga map loaded successfully!");
//            }
//            catch (Exception e) {
//                System.out.println("Error loading map: " + e.getMessage());
//            }
//        }
//        else {
//            System.out.println("mapView is null! Check FXML fx:id");
//        }
//    }
//
//    private void configureWebView() {
//        mapView.setContextMenuEnabled(false);
//        mapView.setZoom(0.95);
//        mapView.setCache(true); }

    private void hideMenuButtonArrow() {
        try {
            menuButton.lookup(".arrow-button").setVisible(false);
            menuButton.lookup(".arrow-button").setManaged(false);
            System.out.println("MenuButton arrow hidden!"); }
        catch (Exception e) {
            System.out.println("Could not hide arrow: " + e.getMessage()); } }

    @FXML private void goToHomepage(MouseEvent event) {
        System.out.println("Navigating to Homepage");
        navigateToPage("homepage.fxml", "Home", event); }

    @FXML private void goToAbout(ActionEvent event) {
        System.out.println("Navigating to About");
        navigateToPage("about-view.fxml", "About", event); }

    @FXML private void goToProfiles(ActionEvent event) {
        System.out.println("Navigating to Profiles");
        navigateToPage("profiles.fxml", "Profiles", event); }

    @FXML private void goToPrograms(ActionEvent event) {
        System.out.println("Navigating to Programs");
        navigateToPage("programs.fxml", "Programs", event); }

    @FXML private void goToStream(ActionEvent event) {
        System.out.println("Navigating to Stream");
        navigateToPage("stream.fxml", "Stream", event); }

    @FXML private void goToNews(ActionEvent event) {
        System.out.println("Navigating to News");
        navigateToPage("news.fxml", "News", event); }

    private void navigateToPage(String fxmlFile, String pageTitle, Object event) {
        try {
            Node sourceNode;

            if (event instanceof MouseEvent) {
                sourceNode = (Node) ((MouseEvent) event).getSource();
            } else if (event instanceof ActionEvent) {
                sourceNode = (Node) ((ActionEvent) event).getSource();
            } else {
                System.out.println("Unknown event type");
                return;
            }

            Stage currentStage = (Stage) sourceNode.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            currentStage.setScene(scene);
            currentStage.setTitle(pageTitle);
            currentStage.show();

            System.out.println("Successfully navigated to: " + pageTitle);

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Navigation Error", "Cannot load " + pageTitle + " page");
        }
    }

    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}