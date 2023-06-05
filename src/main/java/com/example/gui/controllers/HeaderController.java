package com.example.gui.controllers;

import com.example.gui.authenticationStrategy.LoginStrategy;
import com.example.gui.builder.Director;
import com.example.gui.builder.MyFavoritesBuilder;
import com.example.gui.builder.MyRecipesBuilder;
import com.example.gui.builder.RecipesPageBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HeaderController {

    private int user_id;

    public void setUser_id(int userId) {
        this.user_id = userId;
    }

    @FXML
    private Hyperlink fav_link;
    @FXML
    private Hyperlink my_recipes_link;
    @FXML
    private Button logout_btn;
    @FXML
    private ImageView header_logo;


    @FXML
    public void getFavorites(ActionEvent actionEvent) {

        Director d = new Director();
        RecipesPageBuilder b = new MyFavoritesBuilder(fav_link,user_id);
        d.setBuilder(b);
        d.ConstructPage();
    }

    @FXML
    public void getMyRecipes(ActionEvent actionEvent) {

        Director d = new Director();
        RecipesPageBuilder b = new MyRecipesBuilder(my_recipes_link,user_id);
        d.setBuilder(b);
        d.ConstructPage();
    }

    @FXML
    public void logout(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/login.fxml"));
            Parent root = loader.load();
            AuthenticationController controller = loader.getController();
            controller.setStrategy(new LoginStrategy());
            Scene scene = logout_btn.getScene();

            Stage currentStage = (Stage) scene.getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setMaximized(false);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goTohomePage(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/homepage-view.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setUser_id(this.user_id);
            Scene scene = logout_btn.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}