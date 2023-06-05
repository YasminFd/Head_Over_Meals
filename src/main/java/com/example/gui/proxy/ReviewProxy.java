package com.example.gui.proxy;

import com.example.gui.models.Review;

import java.sql.SQLException;

public class ReviewProxy implements ReviewAddition {
        private final Review realReview;
        private final String [] cussWords;

    public Review getReal() {
        return realReview;
    }

    public ReviewProxy(Review real, String[] cussWords) {
            this.realReview = real;
            this.cussWords = cussWords;
        }

        @Override
        public float AddReview() throws SQLException {
            // Check for cuss words before calling the real implementation
            if (containsCussWords(this.realReview.getReview())) {
                return -1;
            }
            // Call the real implementation if no cuss words were found
            return realReview.AddReview();
        }

        private boolean containsCussWords(String content) {
            for (String word : cussWords) {
                if (content.contains(word)) {
                    return true;
                }
            }
            return false;

        }



}
