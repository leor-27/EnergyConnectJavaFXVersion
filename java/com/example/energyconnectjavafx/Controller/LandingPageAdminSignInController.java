package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.Model.Admin;
import com.example.energyconnectjavafx.session.AuthSession;
import com.example.energyconnectjavafx.util.TokenUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class LandingPageAdminSignInController extends NavController {

    @FXML private TextField emailField;

    public void requestAccess() {
        try {
            Admin admin = adminDAO.findUninitializedByEmail(emailField.getText());
            if (emailField.getText() == null || emailField.getText().isBlank()) {
                alert("Email is required.");
                return;
            }

            if (admin == null) {
                alert("If this email exists, an invite will be sent.");
                return;
            }

            String token = TokenUtil.generateToken();
            adminDAO.saveInviteToken(
                    admin.getId(),
                    TokenUtil.hashToken(token),
                    LocalDateTime.now().plusHours(1)
            );

            alert("DEV TOKEN:\n" + token);
            java.awt.Desktop.getDesktop().browse(
                    new java.net.URI("http://localhost/invite?token=" + token)
            );

            AuthSession.setSetupAdminId(admin.getId());
            navigate("landingPageAdminSetCredentials-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Error requesting access.");
        }
    }
}
