package com.example.gui.sortingStrategy;

import com.example.gui.models.Review;

import java.util.List;

public class ReviewSorter {
    private SortingStrategy strategy;

    public ReviewSorter(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(List<Review> reviews) {

        strategy.sort(reviews);
    }
}
