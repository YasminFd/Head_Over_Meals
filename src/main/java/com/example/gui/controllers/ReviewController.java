package com.example.gui.controllers;

import com.example.gui.DBConnection;
import com.example.gui.models.Review;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ReviewController {
    private Review r;
    private float av;
    @FXML
    private Label title,comment;
    @FXML
    private HBox stars;

    public void setReview(Review r,float av) {
        this.r = r;
        this.av=av;
    }
    public void initialize() {
        Platform.runLater(() -> {
            DBConnection db = DBConnection.getInstance();
            title.setText(db.getUserNameFromId(r.getUserId()));
            AddReviewController.setAverageRating(av, stars);
            comment.setText(r.getReview());
        });
    }

}
