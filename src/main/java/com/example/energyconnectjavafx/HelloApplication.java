package com.example.energyconnectjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/com/example/energyconnectjavafx/View/adminHome-view.fxml")
        );

        // Height is 800
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("EnergyConnect");
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}
