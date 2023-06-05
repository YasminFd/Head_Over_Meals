package com.example.gui.builder;

import com.example.gui.controllers.RecipesPageController;

public class Director {
    private RecipesPageBuilder builder;



    public void setBuilder(RecipesPageBuilder builder) {
        this.builder = builder;

    }
    public RecipesPageController getController(){
        return builder.getController();
    }
    public void ConstructPage(){
        builder.CreateNewPage(builder.getU(),builder.getL());
        builder.loadPage();
        builder.setBtnAction();
    }
}
