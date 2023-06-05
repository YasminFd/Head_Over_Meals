package com.example.gui.models;


public class Ingredient {
    private String ingredient;
    private String quantity;
    private int id;

    public Ingredient(String ingredient) {
        this.ingredient = ingredient;
    }
    public Ingredient(String ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
    public Ingredient(int id,String ingredient, String quantity) {
        this.id=id;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredient='" + ingredient + '\'' +
                ", quantity='" + quantity + '\'' +
                '}'+'\n';
    }
}
