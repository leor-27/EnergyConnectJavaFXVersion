package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.DJDAO;
import com.example.energyconnectjavafx.Model.DJ;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileDjBarbieController extends ProfileMainController implements Initializable {

    @FXML private Text djNicknameText;
    @FXML private Text realNameText;
    @FXML private ImageView djPicture;
    @FXML private ImageView leftButton;
    @FXML
    private ImageView rightButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DJ dj = DJDAO.getDJById(3);

        if (dj != null) {
            setCurrentDJ(dj.getId());
            djNicknameText.setText(dj.getStageName());
            realNameText.setText(dj.getRealName());

            String fullPath = "/Applications/XAMPP/htdocs/EnergyConnect/" + dj.getImagePath();
            djPicture.setImage(new Image(new File(fullPath).toURI().toString()));
        }
    }
}
