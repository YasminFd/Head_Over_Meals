package com.example.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class RecipesPageController {
    private int user_id;
    @FXML
    private HeaderController headerController;
    @FXML
    private FlowPane recipeGrid;
    @FXML
    private Button Btn;

    public Button getBtn() {
        return Btn;
    }

    public FlowPane getRecipeGrid() {
        return recipeGrid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        headerController.setUser_id(user_id);
        this.user_id = user_id;
    }


}
