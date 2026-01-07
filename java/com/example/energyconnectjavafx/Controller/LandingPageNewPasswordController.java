package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.session.AuthSession;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class LandingPageNewPasswordController extends NavController {

    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        if (AuthSession.getResetAdminId() == null) {
            navigate("landingPage-view.fxml");
        }
    }

    public void saveNewPassword() {
        if (passwordField.getText().isBlank()) {
            alert("Password is required.");
            return;
        }

        Integer adminId = AuthSession.getResetAdminId();
        if (adminId == null) {
            alert("Unauthorized");
            return;
        }

        try {
            adminDAO.updatePassword(adminId, passwordField.getText());
            AuthSession.clearReset();
            alert("Password reset successful.");
            navigate("landingPage-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Failed to reset password.");
        }
    }
}
