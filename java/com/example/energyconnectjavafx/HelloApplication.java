package com.example.energyconnectjavafx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static HostServices hostServices;

    @Override
    public void start(Stage stage) throws IOException {
        hostServices = getHostServices();
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/com/example/energyconnectjavafx/View/landingPage-view.fxml")
        );

        // Height is 800
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("EnergyConnect");
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public static HostServices getHostServicesInstance() {
        return hostServices;
    }
}
