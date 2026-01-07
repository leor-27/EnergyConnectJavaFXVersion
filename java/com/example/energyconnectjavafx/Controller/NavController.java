package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.AdminDAO;
import com.example.energyconnectjavafx.session.AuthSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavController {

    protected final AdminDAO adminDAO = new AdminDAO();

    protected void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void navigate(String fxmlFile) {
        Stage stage = (Stage) Stage.getWindows()
                .stream()
                .filter(javafx.stage.Window::isShowing)
                .findFirst()
                .orElseThrow();
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/energyconnectjavafx/View/" + fxmlFile)
            );
            Parent newRoot = loader.load();
            stage.getScene().setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
            alert("Navigation error.");
        }
    }

    protected void navigateAdmin(String fxmlFile) {
        if (AuthSession.getLoggedInAdminId() == null) {
            alert("Please log in first.");
            navigate("landingPageAdminLogin-view.fxml");
            return;
        }
        navigate(fxmlFile);
    }

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
        stage.show();
    }

    public void goToLandingPage(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPage-view.fxml");
        System.out.println("Logo / landing page button clicked!");
    }
    public void goToAdminLogin(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPageAdminLogin-view.fxml");
        System.out.println("Admin button login clicked!");
    }
    public void goToAdminSignIn(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPageAdminSignin-view.fxml");
        System.out.println("Admin sign-in button clicked!");
    }
    public void goToAdminSetCredentials(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPageAdminSetCredentials-view.fxml");
        System.out.println("Admin invite dev token clicked!");
    }
    public void goToAdminResetPasswordLink(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPageResetPasswordLink-view.fxml");
        System.out.println("Forgot password button clicked!");
    }
    public void goToNewPassword(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPageNewPassword-view.fxml");
        System.out.println("Reset password dev token clicked!");
    }
    public void goToAdminHome(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "adminHome-view.fxml");
        System.out.println("Logo / Admin Home button clicked!");
    }
    public void goToHomepage(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "homepage-view.fxml");
        System.out.println("User homepage navigation clicked!");
    }
    public void goToAbout(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "about-view.fxml");
        System.out.println("About navigation clicked!");
    }
    public void goToMainProfile(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "profileMain-view.fxml");
        System.out.println("Profiles navigation/button clicked!");
    }
    protected void goToPrograms(Node source) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/energyconnectjavafx/View/program-view.fxml")
            );
            Parent newRoot = loader.load();

            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = stage.getScene();

            scene.setRoot(newRoot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToPrograms(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "program-view.fxml");
        System.out.println("Programs navigation clicked!");
    }
    public void goToStream(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "stream-view.fxml");
        System.out.println("Stream navigation clicked!");
    }
    public void goToNews(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "news-view.fxml");
        System.out.println("News navigation clicked!");
    }
    public void goToAttachNews(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "attachNews-view.fxml");
        System.out.println("Attach news button clicked!");
    }
    public void goToAddPrograms(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "addPrograms-view.fxml");
        System.out.println("Add programs button clicked!");
    }
    public void logout(javafx.scene.input.MouseEvent event) throws IOException {
        switchScene(event, "landingPage-view.fxml");
        System.out.println("Logout button clicked!");
    }
    public void goToAdminHome(Event event) throws IOException {
        switchScene(event, "adminHome-view.fxml");
    }

    @FXML private VBox navMenu;
    @FXML
    private void toggleNavMenu() {
        boolean show = !navMenu.isVisible();
        navMenu.setVisible(show);
        navMenu.setManaged(show);
    }
}
