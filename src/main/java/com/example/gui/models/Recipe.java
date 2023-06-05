package com.example.gui.models;

import com.example.gui.observer.Observer;

import java.io.File;
import java.util.ArrayList;

public class Recipe implements Observer {

    private int rec_id;
    private String name,cuisine,cook_time,author_name;
    private int serving,user_id;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> categories;
    private String instructions;
    private String comment;
    private File image;
    private float average = 0;
    private int reviews_count=0;


    public int getRec_id() {
        return this.rec_id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id= rec_id;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public Recipe() {
    }

    public Recipe(String name, String cuisine, String cook_time, int serving, int user_id, String author_name, ArrayList<Ingredient> ingredients, ArrayList<String> categories, String instructions, String comment, File image) {
        this.name = name;
        this.cuisine = cuisine;
        this.cook_time = cook_time;
        this.serving = serving;
        this.user_id = user_id;
        this.author_name = author_name;
        this.ingredients = ingredients;
        this.categories = categories;
        this.instructions = instructions;
        this.comment = comment;
        this.image = image;
    }

    public Recipe(String name, String cuisine, String cook_time, int serving, ArrayList<Ingredient> ingredients, ArrayList<String> categories, String instructions, String comment) {
        this.name = name;
        this.cuisine = cuisine;
        this.cook_time = cook_time;
        this.serving = serving;
        this.ingredients = ingredients;
        this.categories = categories;
        this.instructions = instructions;
        this.comment = comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
//-->image var+ setters + getters+ add to toString
    //-->add cheff name by getting : int currentUserId = currentUser.getUserId(); when logged in!+ setters and getters and ToString
    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getCook_time() {
        return cook_time;
    }

    public void setCook_time(String cook_time) {
        this.cook_time = cook_time;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "rec_id=" + rec_id +
                ", name='" + name + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", cook_time='" + cook_time + '\'' +
                ", author_name='" + author_name + '\'' +
                ", serving=" + serving +
                ", user_id=" + user_id +
                ", ingredients=" + ingredients +
                ", categories=" + categories +
                ", instructions='" + instructions + '\'' +
                ", comment='" + comment + '\'' +
                ", image=" + image +
                ", average=" + average +
                ", reviews_count=" + reviews_count +
                '}';
    }

    @Override
    public void update(int rate) {
        average = ((average*reviews_count )+(rate * (reviews_count+1)))/(reviews_count*2+1);
        reviews_count++;
    }
}
