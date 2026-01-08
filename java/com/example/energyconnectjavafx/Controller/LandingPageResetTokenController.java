package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.session.AuthSession;
import com.example.energyconnectjavafx.util.TokenUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LandingPageResetTokenController extends NavController {

    @FXML private TextField tokenField;

    public void verifyResetToken() {
        try {
            if (tokenField.getText().isBlank()) {
                alert("Token is required.");
                return;
            }

            Integer adminId = adminDAO.verifyResetToken(
                    TokenUtil.hashToken(tokenField.getText())
            );

            if (adminId == null) {
                alert("Invalid or expired token.");
                return;
            }

            adminDAO.clearResetToken(adminId);
            AuthSession.setResetAdminId(adminId);
            navigate("landingPageNewPassword-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Token verification failed.");
        }
    }
}
