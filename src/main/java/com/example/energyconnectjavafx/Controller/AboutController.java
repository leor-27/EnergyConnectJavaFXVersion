package energyfm.energyconnect.energyconnectjavafxabout.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML private MenuButton menuButton;
    @FXML private ImageView logoHomepage;
    @FXML private WebView mapView;

    @FXML private Hyperlink facebookPageLink;
    @FXML private Hyperlink facebookDJMakisigLink;
    @FXML private Hyperlink tiktokLink;
    @FXML private Hyperlink youtubeLink;
    @FXML private Hyperlink emailLink;
    @FXML private Hyperlink phoneLink;
    @FXML private Hyperlink locationLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("About Page Controller initialized!");

        Platform.runLater(() -> { hideMenuButtonArrow(); });
        loadEnergyFMMap();
    }

    // Sets menuButton arrow as not visible:
    private void hideMenuButtonArrow() {
        try {
            menuButton.lookup(".arrow-button").setVisible(false);
            menuButton.lookup(".arrow-button").setManaged(false);
            System.out.println("MenuButton arrow hidden!"); }
        catch (Exception e) {
            System.out.println("Could not hide arrow: " + e.getMessage()); } }

    @FXML
    private void goToHomepage(MouseEvent event) {
        System.out.println("Navigating Homepage");
        navigateToPage("homepage-view.fxml", "Home", event); }

    @FXML
    private void goToAbout(ActionEvent event) {
        System.out.println("Navigating About Page");
        navigateToPage("about-view.fxml", "About", event); }

    @FXML
    private void goToProfiles(ActionEvent event) {
        System.out.println("Navigating Profiles Page");
        navigateToPage("profiles-view.fxml", "Profiles", event); }

    @FXML
    private void goToPrograms(ActionEvent event) {
        System.out.println("Navigating Programs Page");
        navigateToPage("programs-view.fxml", "Programs", event); }

    @FXML
    private void goToStream(ActionEvent event) {
        System.out.println("Navigating Stream Page");
        navigateToPage("stream-view.fxml", "Stream", event); }

    @FXML
    private void goToNews(ActionEvent event) {
        System.out.println("Navigating News Page");
        navigateToPage("news-view.fxml", "News", event); }

    private void navigateToPage(String fxmlFile, String pageTitle, Object event) {
        try {
            Node sourceNode;

            if (event instanceof MouseEvent) {
                sourceNode = (Node) ((MouseEvent) event).getSource(); }
            else if (event instanceof ActionEvent) {
                sourceNode = (Node) ((ActionEvent) event).getSource(); }
            else {
                System.out.println("Unknown event type");
                return; }

            Stage currentStage = (Stage) sourceNode.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle(pageTitle);
            currentStage.show();

            System.out.println("Successfully navigated to: " + pageTitle); }
        catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Navigation Error", "Cannot load " + pageTitle + " page"); } }

    private void loadEnergyFMMap() {
        if (mapView != null) {
            try {
                System.out.println("Loading map...");

                // Simple test first
                String testHtml = """
                <html>
                <body style="margin:0;padding:0;">
                    <h1>Energy FM Naga</h1>
                    <p>Traders Square, Naga City</p>
                    <div style="background:#ccc;width:100%;height:300px;">
                        Map would appear here
                    </div>
                </body>
                </html>
                """;

                mapView.getEngine().loadContent(testHtml);
                System.out.println("Test HTML loaded");

                // Now try the real map
                String htmlContent = createMapHTML();
                mapView.getEngine().loadContent(htmlContent);
                configureWebView();

                System.out.println("EnergyFM Naga map loaded successfully!");
            }
            catch (Exception e) {
                System.out.println("Error loading map: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            System.out.println("mapView is null!");
        }
    }

    private String createMapHTML() {
        return """
        <!DOCTYPE html>
        <html>
            <head>
                <style>
                    body, html {
                        margin: 0;
                        padding: 0;
                        width: 100%;
                        height: 100%;
                        overflow: hidden;
                    }
                    .map-container {
                        width: 100%;
                        height: 100%;
                    }
                    iframe {
                        width: 100%;
                        height: 100%;
                        border: 0;
                    }
                </style>
            </head>
            <body>
                <div class="map-container">
                    <iframe 
                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d5457.849416366149!2d123.18269377631093!3d13.624453800226762!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x33a18cb151b657bb%3A0x4a9f7a67e5199747!2sTraders%20Square%2C%20Naga%20City%2C%204400%20Camarines%20Sur!5e1!3m2!1sen!2sph!4v1767458804985!5m2!1sen!2sph"
                        allowfullscreen=""
                        loading="lazy"
                        referrerpolicy="no-referrer-when-downgrade">
                    </iframe>
                </div>
            </body>
        </html>
        """;
    }

    private void configureWebView() {
        try {
            // Disable context menu (right-click menu)
            mapView.setContextMenuEnabled(false);

            // Enable JavaScript for Google Maps
            mapView.getEngine().setJavaScriptEnabled(true);

            // Enable caching for performance
            mapView.setCache(true);

        } catch (Exception e) {
            System.out.println("Error configuring WebView: " + e.getMessage());
        }
    }

    // Hyper Links Action Handlers
    @FXML
    private void openFacebookPage(ActionEvent event) {
        openWebPage("https://www.facebook.com/share/1D8mB58KNW/?mibextid=wwXIfr"); }

    @FXML
    private void openFacebookDJMakisig(ActionEvent event) {
        openWebPage("https://www.facebook.com/share/1bGKXAipkL/?mibextid=wwXIfr"); }

    @FXML
    private void openTikTok(ActionEvent event) {
        openWebPage("https://www.tiktok.com/@djmakisig"); }

    @FXML
    private void openYouTube(ActionEvent event) {
        openWebPage("https://youtube.com/@djmakisig?si=i3tHyXQdtRPaq-Jj"); }

    @FXML
    private void openEmail(ActionEvent event) {
        openWebPage("https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&to=energyfmnaga1063@gmail.com"); }

    @FXML
    private void dialPhone(ActionEvent event) {
        openWebPage("tel:+639171137249"); }

    @FXML
    private void openLocation(ActionEvent event) {
        String address = "3F+Traders'+Square+Building,+P.+Burgos+Street,+Brgy.+Sta.+Cruz,+Naga+City,+Philippines";
        openWebPage("https://maps.app.goo.gl/XydzqMZs4kV1EdJe7" + address); }

    // Helper method to open web pages
    private void openWebPage(String url) {
        try {
            System.out.println("Attempting to open URL: " + url);

            // Use java.awt.Desktop if available
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    java.net.URI uri = new java.net.URI(url);
                    desktop.browse(uri);
                    return;
                }
            }

            // Platform-specific fallbacks
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("linux") || os.contains("nix") || os.contains("nux")) {
                // For Ubuntu/Linux
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            } else if (os.contains("win")) {
                // For Windows
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
            } else if (os.contains("mac")) {
                // For macOS
                Runtime.getRuntime().exec(new String[]{"open", url});
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Cannot Open The Link",
                    "Unable to open the web page automatically.\n\n" +
                            "Please copy and paste this URL into your browser:\n\n" + url);
        }
    }

    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}