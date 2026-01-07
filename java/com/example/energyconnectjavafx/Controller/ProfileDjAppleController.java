package com.example.energyconnectjavafx.Controller;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileDjAppleController extends ProfileMainController implements Initializable {

    public Text djNicknameText;
    public Text realNameText;
    public ImageView djPicture;
    public ImageView leftArrow;
    public ImageView rightArrow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (djNicknameText != null) {
            djNicknameText.setText("DJ APPLE");
        }
        if (realNameText != null) {
            realNameText.setText("Laarni Sandia");
        }

        if (leftArrow != null) {
            leftArrow.setOnMouseClicked(event -> {
                try {
                    goToPreviousDJ(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (rightArrow != null) {
            rightArrow.setOnMouseClicked(event -> {
                try {
                    goToNextDJ(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (djPicture != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/images/dj_apple.png"));
                djPicture.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
