package com.example.energyconnectjavafx.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class AddProgramsController extends NavController {
    @FXML
    private ComboBox<String> programType;

    public void initialize() {
        programType.getItems().addAll(
                "MUSIC ONLY",
                "DJ MAKISIG",
                "DJ APPLE",
                "DJ BARBIE",
                "PAPA GATS",
                "KUYA BOK",
                "ARNEL ELCARINAL"
        );
    }
}
