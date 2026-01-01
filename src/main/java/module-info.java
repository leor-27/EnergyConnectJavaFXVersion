module com.example.energyconnectjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.web;
    requires java.sql;

    opens com.example.energyconnectjavafx to javafx.fxml;
    exports com.example.energyconnectjavafx;
    exports com.example.energyconnectjavafx.Controller;
    opens com.example.energyconnectjavafx.Controller to javafx.fxml;
}