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

    public void goToAdminHome() {
        if (emailField.getText().isBlank() || passwordField.getText().isBlank()) {
            alert("Email/username and password are required.");
            return;
        }

        try {
            Admin admin = adminDAO.findByEmailOrUsername(emailField.getText());

            if (admin == null ||
                    !PasswordUtil.verify(passwordField.getText(), admin.getPasswordHash())) {
                alert("Invalid credentials.");
                return;
            }

            if (!admin.isInitialized()) {
                alert("Please use your invite link first.");
                return;
            }

            AuthSession.login(admin.getId());
            navigate("admin-home.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Login failed.");
        }
    }
}
