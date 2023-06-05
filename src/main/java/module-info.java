
    module com.example.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;


    opens com.example.gui to javafx.fxml;
    exports com.example.gui;
    exports com.example.gui.controllers;
    opens com.example.gui.controllers to javafx.fxml;
    exports com.example.gui.models;
    opens com.example.gui.models to javafx.fxml;
    exports com.example.gui.authenticationStrategy;
    opens com.example.gui.authenticationStrategy to javafx.fxml;
    exports com.example.gui.sortingStrategy;
    opens com.example.gui.sortingStrategy to javafx.fxml;
    exports com.example.gui.flyweight;
    opens com.example.gui.flyweight to javafx.fxml;
    exports com.example.gui.proxy;
    opens com.example.gui.proxy to javafx.fxml;
    exports com.example.gui.observer;
    opens com.example.gui.observer to javafx.fxml;
}
