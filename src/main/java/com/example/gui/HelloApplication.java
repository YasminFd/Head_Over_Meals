package com.example.gui;

import com.example.gui.authenticationStrategy.LoginStrategy;
import com.example.gui.controllers.AuthenticationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),370,400);
        AuthenticationController controller = fxmlLoader.getController();
        controller.setStrategy(new LoginStrategy());
        stage.setTitle("Head Over Meals");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}