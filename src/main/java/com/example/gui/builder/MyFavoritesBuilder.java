package com.example.gui.builder;

import com.example.gui.DBConnection;
import com.example.gui.controllers.HomeController;
import com.example.gui.controllers.RecipeController;
import com.example.gui.models.Recipe;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class MyFavoritesBuilder extends RecipesPageBuilder {
    public MyFavoritesBuilder(Node l, int u) {
        super(l, u);
    }

    @Override
    public void loadPage() {
        try {
            DBConnection db = DBConnection.getInstance();
            ArrayList<Recipe> recipes = db.getAllFavoritesForUser(this.controller.getUser_id());
            try {
                for (Recipe R:recipes) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/recipe.fxml"));
                    VBox box = loader.load();
                    RecipeController controller = loader.getController();
                    controller.setPublic(true);
                    controller.setUser_id(this.controller.getUser_id());
                    controller.setData(R);
                    this.controller.getRecipeGrid().getChildren().add(box);
                    FlowPane.setMargin(box, new Insets(10));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.controller.getBtn().setText("Browse Home Page For More Favorites!");
    }


    @Override
        public void setBtnAction() {
            controller.getBtn().setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/homepage-view.fxml"));
                    Parent root = loader.load();
                    HomeController controller = loader.getController();
                    controller.setUser_id(this.u);
                    Scene scene = this.controller.getBtn().getScene();
                    scene.setRoot(root);
                } catch (IOException ev) {
                    ev.printStackTrace();
                }
        });
    }


}
