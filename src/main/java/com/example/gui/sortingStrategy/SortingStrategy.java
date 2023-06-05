package com.example.gui.sortingStrategy;

import com.example.gui.models.Review;

import java.util.List;

public interface SortingStrategy {
    void sort(List<Review> reviews);
}
