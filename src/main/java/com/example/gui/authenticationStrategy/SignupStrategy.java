package com.example.gui.authenticationStrategy;

import com.example.gui.DBConnection;
import com.example.gui.models.User;
import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupStrategy implements AuthenticationStrategy {
    @Override
    public int authenticate(User user) {
        int user_id = -1;


        if(isValidEmail(user.getEmail()) == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please provide a valid email");
            alert.setContentText("Email should be similar to example@gmail.com");
            alert.show();
            return -1;
        }

        if(isValidPassword(user.getPassword()) == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please provide a valid password");
            alert.setContentText("Your password should be a minimum of 8 characters including at least one digit");
            alert.show();
            return -1;
        }

        DBConnection conn = DBConnection.getInstance();
        if(conn.emailExists(user.getEmail())==1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("email already exists!");
            alert.show();
        }
        else {
            user_id = conn.saveUser(user);
        }
        return user_id;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasDigit = true;
                break;
            }
        }

        return hasDigit;
    }
}