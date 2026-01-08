package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.session.AuthSession;
import javafx.event.ActionEvent;
import com.example.energyconnectjavafx.DAO.NewsDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

public class AttachNewsController extends NavController {

    private boolean editMode = false;
    private int editingNewsId;

    @FXML private TextField newsLinkField, headlineField, authorField, orgField, imageUrlField;
    @FXML private TextArea descArea;
    @FXML private Button saveNewsButton;

    // Category Checkboxes
    @FXML private CheckBox catBusiness, catEducation, catEvents, catLocal, catPublicService, catStationNews, catCulture,
            catEntertainment, catHealth, catMusic, catScience, catTechnology, catEconomy,
            catEnvironment, catLifestyle, catPolitics, catSports, catWeather;

    @FXML
    private void handleSaveNews(ActionEvent event) {

        List<String> categories = new ArrayList<>();
        for (CheckBox cb : getAllCategoryCheckboxes()) {
            if (cb.isSelected()) {
                categories.add(cb.getText());
            }
        }

        try {
            Integer adminId = AuthSession.getLoggedInAdminId();

            if (editMode) {
                NewsDAO.updateNews(editingNewsId, newsLinkField.getText(), imageUrlField.getText(),
                        headlineField.getText(), authorField.getText(), orgField.getText(),
                        descArea.getText(), categories
                );
            } else {
                NewsDAO.insertNews(adminId, newsLinkField.getText(), imageUrlField.getText(),
                        headlineField.getText(), authorField.getText(), orgField.getText(),
                        descArea.getText(), categories
                );
            }

            Alert success = new Alert(
                    Alert.AlertType.INFORMATION,
                    editMode ? "News updated successfully." : "News added successfully."
            );
            success.initOwner(saveNewsButton.getScene().getWindow());
            success.showAndWait();

            goToAdminHome(event);

        } catch (Exception e) {
            e.printStackTrace();

            Alert err = new Alert(
                    Alert.AlertType.ERROR,
                    "Database error:\n" + e.getMessage()
            );
            err.initOwner(saveNewsButton.getScene().getWindow());
            err.showAndWait();
        }
    }

    @FXML
    private void handleGoToAddPrograms(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addPrograms.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadForEdit(int newsId) throws SQLException {
        editMode = true;
        editingNewsId = newsId;

        saveNewsButton.setText("Update");

        var news = new NewsDAO().getNewsByIdForAdmin(newsId);

        if (news == null) {
            Alert err = new Alert(Alert.AlertType.ERROR, "Unauthorized edit.");
            err.initOwner(saveNewsButton.getScene().getWindow());
            err.showAndWait();
            return;
        }

        newsLinkField.setText(news.getSourceUrl());
        headlineField.setText(news.getHeadline());
        authorField.setText(news.getAuthor());
        orgField.setText(news.getOrganization());
        descArea.setText(news.getSummary());
        imageUrlField.setText(news.getImagePath());

        List<String> cats = new NewsDAO().getCategoriesForNews(newsId);
        for (CheckBox cb : getAllCategoryCheckboxes()) {
            cb.setSelected(cats.contains(cb.getText()));
        }
    }

    private CheckBox[] getAllCategoryCheckboxes() {
        return new CheckBox[]{
                catBusiness, catEducation, catEvents, catLocal, catPublicService,
                catStationNews, catCulture, catEntertainment, catHealth, catMusic,
                catScience, catTechnology, catEconomy, catEnvironment, catLifestyle,
                catPolitics, catSports, catWeather
        };
    }
}
