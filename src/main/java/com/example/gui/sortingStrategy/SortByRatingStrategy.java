package com.example.gui.sortingStrategy;

import com.example.gui.models.Review;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByRatingStrategy implements SortingStrategy{
    @Override
    public void sort(List<Review> reviews) {
        Collections.sort(reviews, Comparator.comparingInt(Review::getRate).reversed());
    }
}
