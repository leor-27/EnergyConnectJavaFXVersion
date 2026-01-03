package com.example.energyconnectjavafx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ProgramController extends NavController {

//    @FXML
//    private ImageView menuButton;
//
//    @FXML
//    private VBox dropdownMenu;
//
//    @FXML
//    private Text programsLink, aboutLink;
//
//    @FXML
//    public void initialize() {
//        // Toggle dropdown visibility when menu button is clicked
//        menuButton.setOnMouseClicked(event -> dropdownMenu.setVisible(!dropdownMenu.isVisible()));
//
//        // Handle navigation
//        programsLink.setOnMouseClicked(event -> switchScene("/com/example/energyconnectjavafx/View/program-view.fxml"));
//        aboutLink.setOnMouseClicked(event -> switchScene("/com/example/energyconnectjavafx/View/about-view.fxml"));
//    }
//
//    private void switchScene(String fxmlPath) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) menuButton.getScene().getWindow(); // get current stage
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////    }
//protected void switchScene(Event event, String fxmlFile) throws IOException {
//    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/energyconnectjavafx/View/" + fxmlFile));
//    Parent newRoot = loader.load();
//
//    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//    Scene scene = stage.getScene();
//
//    // If scene already exists → just replace content (NO fullscreen exit)
//    if (scene != null) {
//        scene.setRoot(newRoot);
//    }
//    else { // Only happens on first load
//        scene = new Scene(newRoot);
//        stage.setScene(scene);
//    }
//
//    stage.show(); // Fullscreen remains untouched — no flashing
//}
//
//    // If these are used as button actions, they still work because ActionEvent subclasses Event
//    public void goToPrograms(javafx.scene.input.MouseEvent event) throws IOException {
//        switchScene(event, "about-view.fxml");
//        System.out.println("Program navigation clicked!");
//    }
}
