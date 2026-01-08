package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.Model.Admin;
import com.example.energyconnectjavafx.session.AuthSession;
import com.example.energyconnectjavafx.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LandingPageLoginController extends NavController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    public void handleLogin() {
        if (emailField.getText().isBlank() || passwordField.getText().isBlank()) {
            alert("Email/username and password are required.");
            return;
        }

        try {
            Admin admin = adminDAO.findByEmailOrUsername(emailField.getText());

            if (admin == null) {
                alert("Invalid credentials.");
                return;
            }

            String storedHash = admin.getPasswordHash();

            if (storedHash != null && storedHash.startsWith("$2")) {
                alert("This account requires a password reset.");
                navigate("landingPageResetPasswordLink-view.fxml");
                return;
            }

            if (storedHash == null) {
                alert("This account is not yet set up.");
                return;
            }

            if (!PasswordUtil.verify(passwordField.getText(), storedHash)) {
                alert("Invalid credentials.");
                return;
            }

            if (!admin.isInitialized()) {
                alert("Please use your invite link first.");
                return;
            }

            AuthSession.login(admin.getId());
            navigate("adminHome-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Login failed.");
        }
    }
}
