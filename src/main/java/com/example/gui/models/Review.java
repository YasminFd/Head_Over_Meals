package com.example.gui.models;

import com.example.gui.DBConnection;
import com.example.gui.observer.Observer;
import com.example.gui.proxy.ReviewAddition;

import java.sql.SQLException;
import java.sql.Timestamp;

public class Review implements ReviewAddition {
    private String review;
    private int user_id;
    private int rate,recipe;
    private Timestamp Date;
    private Observer rec;



    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public Review(String review, int rate, int user_id, int recipe, Observer r) {
        this.review = review;
        this.rate = rate;
        this.user_id = user_id;
        this.recipe = recipe;
        this.rec=r;
    }

    public Review(String review,  int rate,int user_id, int recipe,Observer recs, Timestamp date) {
        this.review = review;
        this.user_id = user_id;
        this.rate = rate;
        this.recipe = recipe;
        this.rec=recs;
        Date = date;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUser(int user) {
        this.user_id = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "review='" + review + '\'' +
                ", user_id='" + user_id + '\'' +
                ", rate=" + rate +
                ", recipe=" + recipe +
                ", rec=" + rec +
                '}';
    }

    @Override
    public float AddReview() throws SQLException {
        DBConnection d =DBConnection.getInstance();
        d.AddReview(this);
        setDate(new Timestamp(System.currentTimeMillis()));
        return notify( this.getRecipe(),this.getRate());
    }

    public float notify(int rec_id,int rating) throws SQLException {
        DBConnection d =DBConnection.getInstance();
        float x = d.updateAverage(rec_id,rating);
        rec.update(rating);
        return x;
    }
}
