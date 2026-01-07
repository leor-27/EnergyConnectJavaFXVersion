package com.example.energyconnectjavafx.Controller;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileArnelEclarinalController extends ProfileMainController implements Initializable {
    public Text djNicknameText;
    public Text realNameText;
    public ImageView djPicture;
    public ImageView leftArrow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Show DJ's names
        if (djNicknameText != null) {
            djNicknameText.setText("ARNEL ECLARINAL");
        }
        if (realNameText != null) {
            realNameText.setText("Arnel Eclarinal");
        }

        // Make left arrow work (no right arrow on last page)
        if (leftArrow != null) {
            leftArrow.setOnMouseClicked(event -> {
                try {
                    goToPreviousDJ(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Show DJ's picture
        if (djPicture != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/images/arnel_eclarinal.png"));
                djPicture.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
