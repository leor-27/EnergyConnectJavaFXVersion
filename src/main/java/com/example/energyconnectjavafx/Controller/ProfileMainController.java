package energyfm.energyconnect.energyconnectjavafxabout.Controllers;

import energyfm.energyconnect.energyconnectjavafxabout.Controllers.NavController;
import energyfm.energyconnect.energyconnectjavafxabout.DAO.DJDAO;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class ProfileMainController extends NavController {

    // This remembers which DJ we're looking at
    private static int currentDJId = 1; // Start with DJ #1 (DJ MAKISIG)

    // NEW METHOD: When clicking on DJ Makisig image in main profile page
    public void goToFirstDJ(javafx.scene.input.MouseEvent event) throws IOException {
        currentDJId = 1; // Set to DJ Makisig
        changeToDJ(event, 1); // Go to DJ Makisig page
        System.out.println("Going to first DJ: DJ MAKISIG");
    }

    private void changeToDJ(javafx.scene.input.MouseEvent event, int djId) throws IOException {
        currentDJId = djId;

        // Choose which page to show based on DJ ID
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

        // Try all possible paths
        String[] possiblePaths = {
                pageName,
                "/" + pageName,
                "/fxml/" + pageName,
                "/resources/fxml/" + pageName,
                "../fxml/" + pageName
        };

        URL fxmlUrl = null;
        for (String path : possiblePaths) {
            fxmlUrl = getClass().getResource(path);
            if (fxmlUrl != null) {
                System.out.println("FOUND FXML at: " + path);
                break;
            }
        }

        if (fxmlUrl == null) {
            System.out.println("ERROR: FXML not found: " + pageName);

            // Show all available resources
            System.out.println("Available resources:");
            try {
                java.net.URL resourceDir = getClass().getResource("/");
                if (resourceDir != null) {
                    System.out.println("Resource root: " + resourceDir);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Don't return - try to load anyway
            System.out.println("Trying direct path...");
            fxmlUrl = getClass().getClassLoader().getResource(pageName);
        }

        if (fxmlUrl == null) {
            System.out.println("CRITICAL: Cannot find FXML file");
            return;
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

    // ← LEFT ARROW: Go to PREVIOUS DJ
    public void goToPreviousDJ(javafx.scene.input.MouseEvent event) throws IOException {
        // Ask database: "Who comes BEFORE current DJ?"
        int previousDJ = DJDAO.getPrevDJId(currentDJId);

        // Go to that DJ
        changeToDJ(event, previousDJ);

        System.out.println("Going to PREVIOUS DJ: #" + previousDJ);
    }

    // → RIGHT ARROW: Go to NEXT DJ
    public void goToNextDJ(javafx.scene.input.MouseEvent event) throws IOException {
        // Ask database: "Who comes AFTER current DJ?"
        int nextDJ = DJDAO.getNextDJId(currentDJId);

        // Go to that DJ
        changeToDJ(event, nextDJ);

        System.out.println("Going to NEXT DJ: #" + nextDJ);
    }

    // When going back to main profile page, reset to first DJ
    @Override
    public void goToMainProfile(javafx.scene.input.MouseEvent event) throws IOException {
        currentDJId = 1;
        super.goToMainProfile(event);
    }
}