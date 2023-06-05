package com.example.gui.searchStrategy;

import com.example.gui.DBConnection;
import com.example.gui.models.Ingredient;
import com.example.gui.models.Recipe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NameSearchStrategy implements SearchStrategy{

    @Override
    public ArrayList<Recipe> search(String keyword) throws SQLException {
        DBConnection conn = DBConnection.getInstance();
        return conn.searchByName(keyword);
    }
}
