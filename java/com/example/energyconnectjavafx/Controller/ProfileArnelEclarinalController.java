package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.DJDAO;
import com.example.energyconnectjavafx.Model.DJ;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;

public class ProfileArnelEclarinalController extends ProfileMainController implements Initializable {

    @FXML private Text realNameText;
    @FXML private ImageView djPicture;
    @FXML private ImageView leftButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DJ dj = DJDAO.getDJById(6);

        if (dj != null) {
            setCurrentDJ(dj.getId());
            realNameText.setText(
                    dj.getStageName() != null ? dj.getStageName() : "ARNEL ECLARINAL"
            );

            String fullPath = "/Applications/XAMPP/htdocs/EnergyConnect/" + dj.getImagePath();
            djPicture.setImage(new Image(new File(fullPath).toURI().toString()));
        }
    }
}