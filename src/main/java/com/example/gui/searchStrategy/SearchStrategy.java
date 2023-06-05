package com.example.gui.searchStrategy;

import com.example.gui.models.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SearchStrategy {
    public ArrayList<Recipe> search(String keyword) throws SQLException;
}
