package com.example.gui.flyweight;

import com.example.gui.models.Ingredient;

import java.util.HashMap;

public class IngredientFactory {
    private static final HashMap ingredientMap = new HashMap();

    public static Ingredient getIngredient(String ingName) {
        Ingredient ing = (Ingredient) ingredientMap.get(ingName);
        if(ing == null) {
            ing = new Ingredient(ingName);
            ingredientMap.put(ingName,ing);
            System.out.println("creating ingredient "+ ingName);
        }
        else {
            System.out.println("reusing ingredient "+ ingName);
        }
        return ing;
    }
}
