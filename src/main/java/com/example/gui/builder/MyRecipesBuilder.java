package com.example.gui.builder;

import com.example.gui.DBConnection;
import com.example.gui.controllers.AddRecipeController;
import com.example.gui.controllers.RecipeController;
import com.example.gui.models.Recipe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MyRecipesBuilder extends RecipesPageBuilder {
    public MyRecipesBuilder(Node l, int u) {
        super(l, u);
    }

    @Override
    public void loadPage() {
        try {
            DBConnection db = DBConnection.getInstance();
            ArrayList<Recipe> recipes = db.getAllRecipesForUser(this.controller.getUser_id());
            try {
                for (Recipe R:recipes) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/recipe.fxml"));
                    VBox box = loader.load();
                    RecipeController controller = loader.getController();
                    controller.setPublic(false);
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
        this.controller.getBtn().setText("Add More Recipes to Share with Others!");
    }

    @Override
    public void setBtnAction() {
        controller.getBtn().setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/add-recipe-view.fxml"));
                Parent root = loader.load();
                AddRecipeController controller = loader.getController();
                controller.setUser_id(this.u);
                Scene scene = this.controller.getBtn().getScene();
                scene.setRoot(root);
            } catch (IOException ev) {
                ev.printStackTrace();
            }
       });
    }





}
