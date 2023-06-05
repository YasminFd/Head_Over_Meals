package com.example.gui.controllers;

import com.example.gui.DBConnection;
import com.example.gui.models.Recipe;
import com.example.gui.searchStrategy.CategorySearchStrategy;
import com.example.gui.searchStrategy.IngredientSearchStrategy;
import com.example.gui.searchStrategy.NameSearchStrategy;
import com.example.gui.searchStrategy.SearchStrategy;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    //new
    @FXML
    private TextField keywords;

    @FXML
    private ImageView magnifier;

    @FXML
//    private SplitMenuButton filter;
    private ChoiceBox<String> filter;

    private int user_id;
    @FXML
    private HeaderController headerController;
    @FXML
    private Button addRecipe;
    private DBConnection conn = DBConnection.getInstance();

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        headerController.setUser_id(user_id);
        this.user_id = user_id;
    }

    List<Recipe> recipes = new ArrayList<>();

    @FXML
    private FlowPane recipeGrid;

    @FXML
    private Label node;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {


            List<Node> nodesToRemove = new ArrayList<>();
            for (Node node : recipeGrid.getChildren()) {
                if (node instanceof VBox) {
                    nodesToRemove.add(node);
                }
            }
            recipeGrid.getChildren().removeAll(nodesToRemove);

            filter.setValue("Recipe Name");

            this.getRecipes();
            try {
                for (Recipe R : recipes) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/recipe.fxml"));
                    VBox box = loader.load();
                    RecipeController controller = loader.getController();
                    controller.setUser_id(this.user_id);
                    controller.setPublic(true);
                    controller.setData(R);
                    recipeGrid.getChildren().add(box);
                    FlowPane.setMargin(box, new Insets(10));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            recipeGrid.setAlignment(Pos.CENTER);
        });

    }


    public void getRecipes() {

        try {
            recipes = conn.getAllRecipes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRecipe(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/add-recipe-view.fxml"));
            Parent root = loader.load();
            AddRecipeController controller = loader.getController();
            controller.setUser_id(this.user_id);
            controller.initialize();
            Scene scene = addRecipe.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.ENTER) {
            this.handleSearch();
        }
    }
    public void handleSearch(MouseEvent mouseEvent){
        this.handleSearch();
    }
    public void handleSearch() {
        SearchStrategy strategy;
        if (keywords.getText() == "") {
            Notifications notification = Notifications.create()
                    .title("Alert")
                    .text("Please Enter a value to search")
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_CENTER);
            notification.show();
            return;
        }
        if (filter.getValue().equals("Recipe Name")) {
            strategy = new NameSearchStrategy();
        } else if(filter.getValue().equals("Ingredient")){
            strategy = new IngredientSearchStrategy();
        }else{
            strategy = new CategorySearchStrategy();
        }

        ArrayList<Recipe> searchResult = null;
        try {
            searchResult = strategy.search(keywords.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : recipeGrid.getChildren()) {
            if (node instanceof VBox) {
                nodesToRemove.add(node);
            }
        }
        recipeGrid.getChildren().removeAll(nodesToRemove);
        for (Recipe recipe : searchResult) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/recipe.fxml"));
            VBox box = null;
            try {
                box = loader.load();
                RecipeController controller = loader.getController();
                controller.setUser_id(this.user_id);
                controller.setPublic(true);
                controller.setData(recipe);
                recipeGrid.getChildren().add(box);
                FlowPane.setMargin(box, new Insets(10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}