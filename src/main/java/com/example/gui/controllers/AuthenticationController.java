package com.example.gui.controllers;

import com.example.gui.authenticationStrategy.AuthenticationStrategy;
import com.example.gui.authenticationStrategy.LoginStrategy;
import com.example.gui.authenticationStrategy.SignupStrategy;
import com.example.gui.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationController {
    private AuthenticationStrategy strategy;

    public AuthenticationStrategy getStrategy() {
        return this.strategy;
    }

    public void setStrategy(AuthenticationStrategy s) {
        this.strategy = s;
    }

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button login;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button goToLogin, goToSignup;

    public void authenticateUser(ActionEvent actionEvent) throws IOException {
        User user;

        String email = emailField.getText().toString();
        String pass = passwordField.getText().toString();


        if (email.isEmpty() || pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all the fields!");
            alert.show();
            return;
        }

        if(strategy.getClass() == SignupStrategy.class) {
            String username = usernameField.getText().toString();
            String confirmpass = confirmPasswordField.getText().toString();

            if (username.isEmpty() || confirmpass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all the fields!");
                alert.show();
                return;
            }

            if(confirmpass.equals(pass)) {
                user = new User(username,email,pass);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Password confirmation failed!");
                alert.show();
                return ;
            }
        }
        else {
            user = new User(email,pass);
        }

        int user_id = strategy.authenticate(user);
        if(user_id>0) {
            changeScene("/com/example/gui/homepage-view.fxml",emailField, user_id);
        }
    }

    public void goToLogin(ActionEvent actionEvent) throws IOException {
        changeScene("/com/example/gui/login.fxml",goToLogin, new LoginStrategy());
    }

    public void goToSignup(ActionEvent actionEvent) throws IOException {
        changeScene("/com/example/gui/Signup-view.fxml",goToSignup, new SignupStrategy());
    }

    private void changeScene(String fxml, Node node ,int user_id) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //pass the user id to HomeController and HeaderController
        HomeController controller = loader.getController();
        controller.setUser_id(user_id);

        Scene scene = node.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.setMaximized(true);
        scene.setRoot(root);
    }

    public void changeScene(String fxml, Node node, AuthenticationStrategy strategy) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AuthenticationController controller = loader.getController();
        controller.setStrategy(strategy);

        Scene scene = node.getScene();
        Stage currentStage = (Stage) scene.getWindow();
        currentStage.close();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setMaximized(false);
        newStage.setTitle("Head Over Meals");
        newStage.show();
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.ENTER) {
            try {
                this.authenticateUser(new ActionEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
