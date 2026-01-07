package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.session.AuthSession;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LandingPageAdminSetCredentialsController extends NavController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        if (AuthSession.getSetupAdminId() == null) {
            alert("Unauthorized access.");
            navigate("landingPage-view.fxml");
        }
    }

    public void saveCredentials() {
        if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            alert("Username and password are required.");
            return;
        }

        Integer adminId = AuthSession.getSetupAdminId();
        if (adminId == null) {
            alert("Unauthorized");
            return;
        }

        try {
            adminDAO.setCredentials(
                    adminId,
                    usernameField.getText(),
                    passwordField.getText()
            );

            AuthSession.clearSetup();
            AuthSession.login(adminId);
            navigate("admin-home.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Failed to set credentials.");
        }
    }
}
