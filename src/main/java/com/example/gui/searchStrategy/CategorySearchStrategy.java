package com.example.gui.searchStrategy;

import com.example.gui.DBConnection;
import com.example.gui.models.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategorySearchStrategy implements SearchStrategy{

    @Override
    public ArrayList<Recipe> search(String keyword) throws SQLException {
        DBConnection conn = DBConnection.getInstance();
        return conn.searchByCategory(keyword);
    }
}