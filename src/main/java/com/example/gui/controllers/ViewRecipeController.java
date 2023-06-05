package com.example.gui.controllers;

import com.example.gui.DBConnection;
import com.example.gui.models.Ingredient;
import com.example.gui.models.Recipe;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ViewRecipeController {


    DBConnection c = DBConnection.getInstance();

    private int user_id;
    @FXML
    private HeaderController headerController;

    public void setUser_id(int user_id) {
        headerController.setUser_id(user_id);
        this.user_id = user_id;
    }

    private Recipe x;
    //private HBox stars;

    @FXML
    ImageView image;
    @FXML
    Button addReview;
    @FXML
    TextField new_review, new_rate;
    @FXML
    Label recipe_name, error_type;
    @FXML
    Label user, serving, cuisine, time, comment;
    @FXML
    private VBox ingredients, instructions, reviews,addition;
    @FXML
    private HBox categories, rating;

    public void setX(Recipe x) {
        this.x = x;
    }

    public void initialize() {
        DBConnection c = DBConnection.getInstance();
        Platform.runLater(() -> {

            try {
                if(x.getImage()!= null){
                    InputStream f = new FileInputStream(x.getImage());
                    Image im = new Image(f);
                    image.setImage(im);
                }


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }


            for (String a : x.getCategories()) {
                Label cat = new Label(a);
                cat.setStyle("-fx-border-color: #FFC490;\n" +
                        "    -fx-border-style: solid;\n" +
                        "-fx-margin-left: 20px;\n" +
                        "-fx-padding: 5px 10px;\n" +
                        "    -fx-border-width: 2;\n" +
                        "    -fx-text-fill: black;\n" +
                        "    -fx-background-color: transparent;\n" +
                        "    -fx-background-radius: 10;\n" +
                        "    -fx-border-radius: 10;" +
                        "    -fx-font-size:16px;");

                categories.getChildren().add(cat);
            }
            for (Ingredient a : x.getIngredients()) {
                Text ing = new Text(a.getQuantity() + " : " + a.getIngredient());
                ing.setStyle("-fx-text-fill: #4F3726; -fx-padding: 0 0 0 25px;-fx-font-size: 16px;");
                Text bullet1 = new Text("\u2022 "); // Create a bullet point
                ing.setTextAlignment(TextAlignment.JUSTIFY);
                ing.setWrappingWidth(800);
                bullet1.setStyle("-fx-font-size: 18px;");
                HBox hb = new HBox(bullet1,ing);
                hb.setSpacing(5);
                 // set the maximum width of the label
                //ing.setWrapText(true);
                ingredients.getChildren().add(hb);
            }
            String[] step = x.getInstructions().split("\\n");
            int i = 1;
            for (String a : step) {
                Label nb = new Label("Step "+i+":");
                nb.setStyle("-fx-border-color: #FFC490;\n" +
                        "    -fx-border-style: solid;\n" +
                        "-fx-margin-left: 20px;\n" +
                        "-fx-padding: 5px 10px;\n" +
                        "    -fx-border-width: 2;\n" +
                        "    -fx-text-fill: black;\n" +
                        "    -fx-background-color: transparent;\n" +
                        "    -fx-background-radius: 10;\n" +
                        "    -fx-border-radius: 10;" +
                        "    -fx-font-size:16px;" +
                        "    -fx-background-color: #ffffff;" +
                        " -fx-font-weight: bold;");
                Text st = new Text(a);
                st.setStyle("-fx-padding: 0 0 0 30;-fx-font-size: 16px;");
                st.setTextAlignment(TextAlignment.JUSTIFY);
                st.setWrappingWidth(800);
                HBox space= new HBox(st);
                space.setPadding(new Insets(0,0,0,50));
                i++;
                VBox v = new VBox(nb,space);
                v.setSpacing(5);
                instructions.getChildren().add(v);
            }
            recipe_name.setText(x.getName());
            cuisine.setText(x.getCuisine());
            time.setText(x.getCook_time());
            user.setText(x.getAuthor_name());
            comment.setText(x.getComment());
            serving.setText(String.valueOf(x.getServing()));

            float z = 0;
            z = x.getAverage();
            if (z != 0)
                AddReviewController.setAverageRating(z, rating);
            else {
                Label l = new Label("no reviews yet");
                rating.getChildren().add(l);
            }
            addReview.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/addReview.fxml"));
                    VBox container_add = loader.load();
                    addition.getChildren().add(container_add);
                    AddReviewController controller = loader.getController();
                   // x.setAuthor_name(c.getUserNameFromId(x.getUser_id()));
                    controller.setData(x);
                    controller.setUser_id(this.user_id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addReview.setVisible(false);
            });


        });
    }

    public HBox getRating() {
        return rating;
    }


    public void AddToFavorites(MouseEvent mouseEvent) {
        int result = c.addRecToFavorites(this.x.getRec_id(), this.user_id);
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
        }
    }
}
