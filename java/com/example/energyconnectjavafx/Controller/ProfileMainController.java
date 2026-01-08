package com.example.energyconnectjavafx.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class ProfileMainController extends NavController {

    private static int currentDJId = 1;
    private static final int[] DJ_ORDER = {1, 2, 3, 4, 5, 6};

    public void goToFirstDJ(javafx.scene.input.MouseEvent event) throws IOException {
        currentDJId = 1; // Set to DJ Makisig
        changeToDJ(event, 1);
        System.out.println("Going to first DJ: DJ MAKISIG");
    }

    private void changeToDJ(javafx.scene.input.MouseEvent event, int djId) throws IOException {
        currentDJId = djId;

        String pageName = "";
        switch(djId) {
            case 1: pageName = "profileDjMakisig-view.fxml"; break;
            case 2: pageName = "profileDjApple-view.fxml"; break;
            case 3: pageName = "profileDjBarbie-view.fxml"; break;
            case 4: pageName = "profilePapaGats-view.fxml"; break;
            case 5: pageName = "profileKuyaBok-view.fxml"; break;
            case 6: pageName = "profileArnelEclarinal-view.fxml"; break;
        }

        System.out.println("Looking for FXML: " + pageName);

        URL fxmlUrl = getClass().getResource(
                "/com/example/energyconnectjavafx/View/" + pageName
        );

        if (fxmlUrl == null) {
            throw new IOException("FXML not found: " + pageName);
        }

        try {
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent newPage = loader.load();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.getScene().setRoot(newPage);
            System.out.println("Successfully loaded: " + pageName);
        } catch (Exception e) {
            System.out.println("Failed to load FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToNextDJ(javafx.scene.input.MouseEvent event) throws IOException {
        int nextId = currentDJId;
        for (int i = 0; i < DJ_ORDER.length; i++) {
            if (DJ_ORDER[i] == currentDJId && i < DJ_ORDER.length - 1) {
                nextId = DJ_ORDER[i + 1];
                break;
            }
        }
        changeToDJ(event, nextId);
    }

    public void goToPreviousDJ(javafx.scene.input.MouseEvent event) throws IOException {
        int prevId = currentDJId;
        for (int i = 0; i < DJ_ORDER.length; i++) {
            if (DJ_ORDER[i] == currentDJId && i > 0) {
                prevId = DJ_ORDER[i - 1];
                break;
            }
        }
        changeToDJ(event, prevId);
    }

    // When going back to main profile page, reset to first DJ
    @Override
    public void goToMainProfile(javafx.scene.input.MouseEvent event) throws IOException {
        currentDJId = 1;
        super.goToMainProfile(event);
    }

    protected void setCurrentDJ(int djId) {
        currentDJId = djId;
    }
}
