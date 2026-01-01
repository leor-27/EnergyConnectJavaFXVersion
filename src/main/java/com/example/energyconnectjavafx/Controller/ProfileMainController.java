package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.Model.DBConnection;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMainController extends NavController {

    // Accept any Event (ActionEvent, MouseEvent, etc.)
    protected void switchScene(Event event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/energyconnectjavafx/View/" + fxmlFile));
        Parent newRoot = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();

        // If scene already exists → just replace content (NO fullscreen exit)
        if (scene != null) {
            scene.setRoot(newRoot);
        }
        else { // Only happens on first load
            scene = new Scene(newRoot);
            stage.setScene(scene);
        }

        stage.show(); // Fullscreen remains untouched — no flashing
    }

    public void loadDJProfile(String djName) {
        String query = "SELECT * FROM djs WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, djName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("DJ Name: " + rs.getString("name"));
                System.out.println("DJ Show: " + rs.getString("show_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // If these are used as button actions, they still work because ActionEvent subclasses Event
    public void goToDJMakisig(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profileDjMakisig-view.fxml");
        System.out.println("DJ Makisig clicked!");
    }
    public void goToDJApple(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profileDjApple-view.fxml");
        System.out.println("DJ Apple clicked!");
    }
    public void goToDJBarbie(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profileDjBarbie-view.fxml");
        System.out.println("DJ Barbie clicked!");
    }
    public void goToPapaGats(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profilePapaGats-view.fxml");
        System.out.println("Papa Gats clicked!");
    }
    public void goToKuyaBok(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profileKuyaBok-view.fxml");
        System.out.println("Kuya Bok clicked!");
    }
    public void goToArnelElcarinal(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profileArnelElcarinal-view.fxml");
        System.out.println("Arnel Elcarinal clicked!");
    }
}