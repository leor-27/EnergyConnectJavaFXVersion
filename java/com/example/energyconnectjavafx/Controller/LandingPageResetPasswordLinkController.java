package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.Model.Admin;
import com.example.energyconnectjavafx.util.TokenUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LandingPageResetPasswordLinkController extends NavController {

    @FXML private TextField emailField;

    public void sendResetLink() {
        if (emailField.getText().isBlank()) {
            alert("Email is required.");
            return;
        }

        try {
            Admin admin = adminDAO.findInitializedByEmailOrUsername(emailField.getText());
            if (admin == null) {
                alert("If this email exists, a reset link will be sent.");
                return;
            }

            String token = TokenUtil.generateToken();
            adminDAO.saveResetToken(admin.getId(), TokenUtil.hashToken(token));

            alert(
                    "DEV RESET TOKEN:\n" + token +
                            "\n\nPaste this into the Reset Token screen."
            );
            navigate("landingPageResetToken-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            alert("Reset failed.");
        }
    }
}
