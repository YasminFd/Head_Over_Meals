package com.example.gui.builder;

import com.example.gui.controllers.RecipesPageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class RecipesPageBuilder {
    RecipesPageController controller;
    Node l;
    int u;

    public Node getL() {
        return l;
    }

    public int getU() {
        return u;
    }

    public RecipesPageBuilder(Node l, int u) {
        this.l = l;
        this.u = u;
    }

    public RecipesPageController getController() {
        return controller;
    }

    public void CreateNewPage(int user, Node link) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/Recipes-Page-View.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setUser_id(user);
            l=link;
            u=user;
            Scene scene = link.getScene();
            scene.setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public abstract void loadPage();

    public abstract void setBtnAction();

}
