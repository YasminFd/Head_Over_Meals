package com.example.gui.authenticationStrategy;

import com.example.gui.DBConnection;
import com.example.gui.models.User;
import javafx.scene.control.Alert;

public class LoginStrategy implements AuthenticationStrategy {
    @Override
    public int authenticate(User user) {
        //check if user credentials exist and valid
        DBConnection conn = DBConnection.getInstance();
        int verify = conn.verifyLogin(user.getEmail(),user.getPassword());
        if(verify>0) {
            return verify;

        }
        else if(verify == 0) {
        }
        else {
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Provided credentials are incorrect!");
        alert.show();
        return 0;
    }
}