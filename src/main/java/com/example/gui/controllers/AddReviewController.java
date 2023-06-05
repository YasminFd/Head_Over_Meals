package com.example.gui.controllers;

import com.example.gui.*;
import com.example.gui.models.Recipe;
import com.example.gui.models.Review;
import com.example.gui.proxy.ReviewProxy;
import com.example.gui.sortingStrategy.ReviewSorter;
import com.example.gui.sortingStrategy.SortByDateStrategy;
import com.example.gui.sortingStrategy.SortByRatingStrategy;
import com.example.gui.sortingStrategy.SortingStrategy;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddReviewController {

    private int user_id;

    public void setUser_id(int id) {
        this.user_id = id;
    }

    private Recipe x;
    @FXML
    private ChoiceBox<String> sortBy;
    @FXML
    private ImageView image;
    @FXML
    private TextArea new_review;
    @FXML
    private ChoiceBox new_rate;
    @FXML
    private Label recipe_name, error_type;
    @FXML
    private VBox reviews;

    private ArrayList<Review> comments;
    private DBConnection conn = DBConnection.getInstance();

    public void setData(Recipe R){
        this.x=R;
    }
    public void initialize() {
        Platform.runLater(() -> {
            //AtomicInteger f= new AtomicInteger();
            sortBy.setOnAction(e -> {

                int sort = 0;
                ArrayList<Review> tmp = reviews();
                if(sortBy.getValue().equals("Rating") == true)
                {
                    sort = 1;
                }
                comments=new ArrayList<>();
                load(tmp,sort);
                comments = tmp;
            });
            sortBy.show();
            sortBy.setValue("Date");
            sortBy.hide();
        });
    }

    @FXML
    protected void AddComment(ActionEvent actionEvent) throws SQLException, IOException {
        String [] cuss;
        String s ="shit ugly damn ew yuk disgusting";
        cuss = s.split(" ");

        if(conn.checkIfUserHasReviews(user_id,x.getRec_id()) == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You already posted a review on this recipe!");
            alert.show();
            return;
        }

        ReviewProxy r= new ReviewProxy(new Review(new_review.getText().toString(),Integer.parseInt(new_rate.getValue().toString()),this.user_id,x.getRec_id(),x),cuss);
        Float av = r.AddReview();
        if(av == -1){
            error_type.setText("plz mind ur words");
            return;
        }else if(av ==0){
            error_type.setText("failure");
            return;
        }else{
            sortBy.show();
            sortBy.setValue("Date");
            sortBy.hide();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Review Added Successfully!");
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/RecipeView.fxml"));
            Parent root = loader.load();
            ViewRecipeController g = loader.getController();
            g.setX(x);
            g.setUser_id(user_id);
            Scene scene = sortBy.getScene();
            scene.setRoot(root);
        }
    }



    static void setAverageRating(float av, HBox h) {
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
    private int load(List comments,int sort) {

        int hasReview = 0;
        if (sort == 0) {
            SortingStrategy strategy = new SortByDateStrategy();
            ReviewSorter sorter = new ReviewSorter(strategy);
            sorter.sort(comments);
        }else {
            SortingStrategy strategy = new SortByRatingStrategy();
            ReviewSorter sorter = new ReviewSorter(strategy);
            sorter.sort(comments);
        }
        reviews.getChildren().clear();
        for (int i = 0; i < comments.size(); i++) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/review.fxml"));
            VBox container = null;
            try {
                container = loader.load();
                ReviewController controller = loader.<ReviewController>getController();
                Review v= (Review) comments.get(i);
                controller.setReview(v,v.getRate());
                reviews.getChildren().add(container);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return  0;
    }


    private ArrayList<Review> reviews()
    {
        ArrayList<Review> rl;
        try {
            rl=conn.getAllReviewsForRecipe(x.getRec_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rl;
    }
}
