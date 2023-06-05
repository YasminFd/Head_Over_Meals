package com.example.gui.controllers;

import com.example.gui.DBConnection;
import com.example.gui.builder.Director;
import com.example.gui.builder.MyFavoritesBuilder;
import com.example.gui.builder.MyRecipesBuilder;
import com.example.gui.builder.RecipesPageBuilder;
import com.example.gui.models.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class RecipeController {

    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private boolean isPublic;
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }


    DBConnection connection = DBConnection.getInstance();

    @FXML
    private Label Rname;

    @FXML
    private Label category;

    @FXML
    private ImageView favorite;

    @FXML
    private Label nbRatings;

    @FXML
    private ImageView profile;

    @FXML
    private ImageView recipeImage;


    @FXML
    private VBox recipe;

    @FXML
    private Button edit_button,delete_button;
    @FXML
    private HBox rate;

    @FXML
    private Recipe r;

    @FXML
    private void handleMouseEntered(MouseEvent event) {
        favorite.setCursor(Cursor.HAND);
    }

    @FXML
    private void addToFav(MouseEvent event)
    {
        int result = connection.addRecToFavorites(this.r.getRec_id(), this.user_id);
        if( result==1 ) {
            Notifications notification = Notifications.create()
                    .title("Success!")
                    .text("Recipe added to favorites!")
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_CENTER);
            notification.show();
        }
        else {
            Notifications notification = Notifications.create()
                    .title("Success!")
                    .text("Recipe removed from favorites!")
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_CENTER);
            notification.show();

            Director d = new Director();
            RecipesPageBuilder b = new MyFavoritesBuilder(favorite,user_id);
            d.setBuilder(b);
            d.ConstructPage();
        }
    }

    @FXML
    private void handleBoxClicked(MouseEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/RecipeView.fxml"));
            Parent root = loader.load();
            ViewRecipeController controller = loader.getController();
            controller.setUser_id(this.user_id);
            controller.setX(r);
            Scene scene = recipeImage.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void setData(Recipe recipe) throws FileNotFoundException {
        this.r=recipe;
        InputStream f = new FileInputStream(recipe.getImage());
        Image image= new Image(f);
        this.recipeImage.setImage(image);

        this.Rname.setText(recipe.getName());
        this.category.setText(recipe.getCuisine());
        this.nbRatings.setText(String.valueOf(recipe.getReviews_count()));

        favorite.setVisible(isPublic);
        nbRatings.setVisible(isPublic);
        if(isPublic)
        {
            edit_button.setVisible(false);
            delete_button.setVisible(false);
        }
        else
        {
            edit_button.setVisible(true);
            delete_button.setVisible(true);
        }

        if(isPublic) {
               setAverageRating(recipe.getAverage(), rate);
        }
        else {
            edit_button.setMinSize(50,30);
            edit_button.setOnAction(e->editRecipe(recipe));
            delete_button.setMinSize(50,30);
            delete_button.setOnAction(e->deleteRecipe(recipe));
        }

    }

    private void deleteRecipe(Recipe recipe) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to continue?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            // User clicked OK button, perform action
            connection.deleteRecipe(recipe.getRec_id());
            Director d = new Director();
            RecipesPageBuilder b = new MyRecipesBuilder(delete_button,user_id);
            d.setBuilder(b);
            d.ConstructPage();
            Notifications notification = Notifications.create()
                    .title("Success!")
                    .text("Recipe was deleted successfully!")
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_CENTER);
            notification.show();
        }

    }

    private void setAverageRating(float av, HBox h) {
        h.getChildren().clear();
        int z = 1;
        while (av >= 1) {
            Image i = new Image("com/example/gui/images/star.png");
            ImageView m = new ImageView();
            m.setImage(i);
            h.getChildren().add(m);
            av -= 1;
            z++;
        }
        if (av != Math.floor(av)) {
            Image i = new Image("com/example/gui/images/half-star-shape.png");
            ImageView m = new ImageView();
            m.setImage(i);
            h.getChildren().add(m);
            z++;
        }
        while (z <= 5) {
            Image i = new Image("com/example/gui/images/star-gray.png");
            ImageView m = new ImageView();
            m.setImage(i);
            h.getChildren().add(m);
            av -= 1;
            z++;
        }


    }
    public void editRecipe(Recipe recipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/add-recipe-view.fxml"));
            Parent root = loader.load();
            AddRecipeController controller = loader.getController();
            controller.setUser_id(this.user_id);
            controller.setR(recipe);
            Scene scene = Rname.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHover(MouseEvent mouseEvent) {
        Rname.setStyle("-fx-underline: true;");
    }
    public void offHover(MouseEvent mouseEvent) {
        Rname.setStyle("-fx-underline: false;");
    }
}
