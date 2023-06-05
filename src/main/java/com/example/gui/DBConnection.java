package com.example.gui;

import com.example.gui.flyweight.IngredientFactory;
import com.example.gui.models.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection {

    private static DBConnection instance;
    private static Connection dbconn;

    private DBConnection() {
        String dbName = "homdb";
        String dbUser = "root";
        String dbPass = "";
        String url = "jdbc:mysql://127.0.0.1/" + dbName;

        try{
            dbconn = DriverManager.getConnection(url,dbUser,dbPass);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if(instance==null){
            instance = new DBConnection();
        }
        return instance;
    }


    public int saveRecipe(Recipe recipe) throws SQLException{
        try {
            PreparedStatement stmt = dbconn.prepareStatement("INSERT INTO recipes (rec_name, steps, comments, image, servings, author_id, created_at, cuisine, cooking_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getInstructions());
            stmt.setString(3, recipe.getComment());
            // Set the image blob
            if(recipe.getImage() != null) {
                FileInputStream f = new FileInputStream(String.valueOf(recipe.getImage()));
                stmt.setBinaryStream(4,f,f.available());
            } else {
                stmt.setBytes(4, null);
            }
            stmt.setInt(5, recipe.getServing());
            stmt.setInt(6, recipe.getUser_id());
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            stmt.setString(8, recipe.getCuisine());
            stmt.setString(9, recipe.getCook_time());
            stmt.executeUpdate();

            // Get the generated recipe ID
            // Get the generated recipe ID
            ResultSet rs = stmt.getGeneratedKeys();
            int rec_id = -1;
            if (rs.next()) {
                rec_id = rs.getInt(1);
            }

            // Insert the recipe-ingredient relations

            PreparedStatement stmt2 = dbconn.prepareStatement("INSERT INTO recipe_ingredients (rec_id, ing_id, quantity) VALUES (?, ?, ?)");
            for (Ingredient ing : recipe.getIngredients()) {
                int ing_id = -1;
                // Check if the ingredient exists in the database
                Ingredient existingIng = getIngredientByName(ing.getIngredient());
                if (existingIng != null) {
                    existingIng.setQuantity(ing.getQuantity());
                    ing_id = existingIng.getId();
                } else {
                    // Insert the new ingredient and get the generated ID
                    PreparedStatement stmt4 = dbconn.prepareStatement("INSERT INTO ingredients (ing_name, ing_health_info) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
                    stmt4.setString(1, ing.getIngredient());
                    stmt4.setString(2, "N/A");
                    stmt4.executeUpdate();
                    ResultSet rs2 = stmt4.getGeneratedKeys();
                    if (rs2.next()) {
                        ing_id = rs2.getInt(1);
                    }
                }
                stmt2.setInt(1, rec_id);
                stmt2.setInt(2, ing_id);
                stmt2.setString(3, ing.getQuantity());
                stmt2.executeUpdate();
            }

            // Insert the recipe-category relations
            PreparedStatement stmt3 = dbconn.prepareStatement("INSERT INTO recipes_category (rec_id, cat_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            for (String cat : recipe.getCategories()) {
                int cat_id = -1;
                // Check if the category exists in the database
                Category existingCat = getCategoryByName(cat);
                if (existingCat != null) {
                    cat_id = existingCat.getId();
                } else {
                    // Insert the new category and get the generated ID
                    PreparedStatement stmt5 = dbconn.prepareStatement("INSERT INTO categories (cat_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                    stmt5.setString(1, cat);
                    stmt5.executeUpdate();
                    ResultSet rs3 = stmt5.getGeneratedKeys();
                    if (rs3.next()) {
                        cat_id = rs3.getInt(1);
                    }
                }
                stmt3.setInt(1, rec_id);
                stmt3.setInt(2, cat_id);
                stmt3.executeUpdate();
            }
            return rs.getInt(1);

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Category getCategoryByName(String name) {
        Category category = null;
        try {
            PreparedStatement stmt = dbconn.prepareStatement("SELECT * FROM categories WHERE cat_name = ?", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                category = new Category(rs.getInt("cat_id"), rs.getString("cat_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    public Ingredient getIngredientByName(String name) {
        Ingredient ing = null;
        try {
            PreparedStatement stmt = dbconn.prepareStatement("SELECT * FROM ingredients WHERE ing_name = ?", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ing = new Ingredient(rs.getInt("ing_id"), rs.getString("ing_name"),"0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ing;
    }

    public void updateRecipe(Recipe updatedRecipe) {
        try  {
            // Update the recipe
            String updateRecipeSql = "UPDATE recipes SET rec_name=?, average=?, rev_count=?, steps=?, comments=?, image=?, servings=?, author_id=?, cuisine=?, cooking_time=? WHERE rec_id=?";
            try (PreparedStatement stmt = dbconn.prepareStatement(updateRecipeSql)) {
                stmt.setString(1, updatedRecipe.getName());
                stmt.setDouble(2, updatedRecipe.getAverage());
                stmt.setInt(3, updatedRecipe.getReviews_count());
                stmt.setString(4, updatedRecipe.getInstructions());
                stmt.setString(5, updatedRecipe.getComment());
                if(updatedRecipe.getImage() != null) {
                    FileInputStream f = new FileInputStream(String.valueOf(updatedRecipe.getImage()));
                    stmt.setBinaryStream(6,f,f.available());
                } else {
                    stmt.setBytes(6, null);
                }
                stmt.setInt(7, updatedRecipe.getServing());
                stmt.setInt(8, updatedRecipe.getUser_id());
                stmt.setString(9, updatedRecipe.getCuisine());
                stmt.setString(10, updatedRecipe.getCook_time());
                stmt.setInt(11, updatedRecipe.getRec_id());
                stmt.executeUpdate();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Update the ingredients
            String deleteIngredientsSql = "DELETE FROM recipe_ingredients WHERE rec_id=?";
            try (PreparedStatement stmt = dbconn.prepareStatement(deleteIngredientsSql)) {
                stmt.setInt(1, updatedRecipe.getRec_id());
                stmt.executeUpdate();
            }

            String insertIngredientSql = "INSERT INTO recipe_ingredients (rec_id, ing_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = dbconn.prepareStatement(insertIngredientSql)) {
                for (Ingredient ingredient : updatedRecipe.getIngredients()) {
                    stmt.setInt(1, updatedRecipe.getRec_id());
                    stmt.setInt(2, getIngredientId(ingredient.getIngredient()));
                    stmt.setString(3, ingredient.getQuantity());
                    stmt.executeUpdate();
                }
            }

            // Update the categories
            String deleteCategoriesSql = "DELETE FROM recipes_category WHERE rec_id=?";
            try (PreparedStatement stmt = dbconn.prepareStatement(deleteCategoriesSql)) {
                stmt.setInt(1, updatedRecipe.getRec_id());
                stmt.executeUpdate();
            }

            String insertCategorySql = "INSERT INTO recipes_category (rec_id, cat_id) VALUES (?, ?)";
            try (PreparedStatement stmt = dbconn.prepareStatement(insertCategorySql)) {
                for (String category : updatedRecipe.getCategories()) {
                    stmt.setInt(1, updatedRecipe.getRec_id());
                    stmt.setInt(2, getCategoryId(category));
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private int getCategoryId( String categoryName) throws SQLException {
        // Check if category exists
        PreparedStatement ps = dbconn.prepareStatement("SELECT cat_id FROM categories WHERE cat_name = ?", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, categoryName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("cat_id");
        } else {
            PreparedStatement stmt5 = dbconn.prepareStatement("INSERT INTO categories (cat_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt5.setString(1, categoryName);
            stmt5.executeUpdate();
            ResultSet rs3 = stmt5.getGeneratedKeys();
            if (rs3.next()) {
                return rs3.getInt(1);
            }
        }
        return -1;
    }

    private int getIngredientId( String ingredientName) throws SQLException {
        // Check if ingredient exists
        PreparedStatement ps = dbconn.prepareStatement("SELECT ing_id FROM ingredients WHERE ing_name = ?", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ingredientName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("ing_id");
        } else {
            PreparedStatement stmt4 = dbconn.prepareStatement("INSERT INTO ingredients (ing_name, ing_health_info) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt4.setString(1, ingredientName);
            stmt4.setString(2, "N/A");
            stmt4.executeUpdate();
            ResultSet rs2 = stmt4.getGeneratedKeys();
            if (rs2.next()) {
                return rs2.getInt(1);
            }
        }
        return -1;
    }
    public ArrayList<Review> getAllReviewsForRecipe(int id) throws SQLException{
        ArrayList<Review> r = new ArrayList<>();
        System.out.println("hello"+id);
        PreparedStatement rev = dbconn.prepareStatement("SELECT r.* FROM reviews r WHERE r.rec_id = ?");
        rev.setInt(1, id);
        ResultSet fs = rev.executeQuery();
        while(fs.next()) {
            r.add(new Review(fs.getString("r_text"),fs.getInt("rating"),fs.getInt("user_id"),fs.getInt("user_id"),getRecipeById(id),fs.getTimestamp("added_at")));
        }
        return r;
    }

    public void AddReview(Review r) {
        PreparedStatement rev = null;
        try {
            rev = dbconn.prepareStatement("INSERT INTO reviews(r_text,user_id,rating,rec_id,added_at) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            rev.setString(1,r.getReview());
            rev.setInt(2,r.getUserId());
            rev.setInt(3,r.getRate());
            rev.setInt(4,r.getRecipe());
            rev.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            rev.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public Recipe getRecipeById(int i) {
        try {
            Recipe recipe = new Recipe();
            PreparedStatement stmt = dbconn.prepareStatement("SELECT rec_name,rec_id, steps, comments, image, servings, average, rev_count, author_id, created_at, cuisine, cooking_time FROM recipes WHERE rec_id = ?", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();
            // Set the image blob
            if (rs.next()) {
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));
                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                recipe.setRec_id(i);
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();
                File f = new File("image_file.jpg");
                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));

                ArrayList<String> categories = new ArrayList<>();
                //
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                //
                recipe.setIngredients(ingredients);
                return recipe;

            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Recipe> getAllRecipes() throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();


        int v=0;


        String query = "SELECT * FROM recipes";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                v++;


                Recipe recipe = new Recipe();
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));
                recipe.setRec_id(rs.getInt("rec_id"));
                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();

                //added
                File f = new File("image_file"+v+".jpg");


                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));
                ArrayList<String> categories = new ArrayList<>();
                //
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                //
                recipe.setIngredients(ingredients);
                // Add the recipe object to the list
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }

        return recipes;
    }
    public ArrayList<Recipe> getAllRecipesForUser(int id) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();

        //added
        int v =0;

        String query = "SELECT * FROM recipes WHERE author_id = ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                //added
                v++;


                Recipe recipe = new Recipe();
                recipe.setUser_id(id);
                recipe.setRec_id(rs.getInt("rec_id"));
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));

                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();

                //added

                File f = new File("image_file"+v+".jpg");


                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));
                ArrayList<String> categories = new ArrayList<>();
                //
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                //
                recipe.setIngredients(ingredients);
                // Add the recipe object to the list
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }

        return recipes;
    }
    public ArrayList<Recipe> getAllFavoritesForUser(int id) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();

        int v=0;
        String query = "SELECT * FROM recipes r  WHERE rec_id IN (SELECT rec_id FROM favorites WHERE user_id = ?)";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ///added

                v++;


                Recipe recipe = new Recipe();
                recipe.setUser_id(id);
                recipe.setRec_id(rs.getInt("rec_id"));
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));

                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();
                ///added

                File f = new File("image_file"+v+".jpg");


                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));

                ArrayList<String> categories = new ArrayList<>();
                //
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                //
                recipe.setIngredients(ingredients);
                // Add the recipe object to the list
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }

        return recipes;
    }

    public float updateAverage(int recId, int rating) throws SQLException {

        PreparedStatement rec = dbconn.prepareStatement("UPDATE recipes SET average = ((average*rev_count )+( ? * (rev_count + 1)))/(rev_count*2+1) WHERE rec_id = ?", Statement.RETURN_GENERATED_KEYS);
        rec.setInt(1,rating);
        rec.setInt(2,recId);
        rec.executeUpdate();
        rec = dbconn.prepareStatement("UPDATE recipes SET rev_count = rev_count + 1 WHERE rec_id = ?", Statement.RETURN_GENERATED_KEYS);
        rec.setInt(1,recId);
        rec.executeUpdate();
        return getAverage(recId);
    }
    public float getAverage(int recId) throws SQLException {
        PreparedStatement rec = dbconn.prepareStatement("SELECT average from recipes WHERE rec_id = ? ", Statement.RETURN_GENERATED_KEYS);
        rec.setInt(1,recId);
        ResultSet s = rec.executeQuery();
        if(s.next())
            return s.getFloat("average");
        return 0;
    }

    public ArrayList<String> getAllCatgoriesForRecipe(int rec_id) throws SQLException {
        ArrayList<String> categories = new ArrayList<>();
        PreparedStatement cat = dbconn.prepareStatement("SELECT cat_name FROM categories WHERE cat_id IN (SELECT cat_id FROM recipes_category WHERE rec_id = ?) ");
        cat.setInt(1, rec_id);
        ResultSet fs = cat.executeQuery();
        while(fs.next()) {
            categories.add(fs.getString("cat_name"));
        }
        return categories;
    }
    public ArrayList<Ingredient> getAllIngredientsForRecipe(int rec_id) throws SQLException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        PreparedStatement ing = dbconn.prepareStatement("SELECT i.ing_name, i.ing_id , r.quantity FROM ingredients i, recipe_ingredients r WHERE  r.rec_id = ? and i.ing_id = r.ing_id ");
        ing.setInt(1, rec_id);
        ResultSet ls = ing.executeQuery();
        while(ls.next()) {
            Ingredient newIng = IngredientFactory.getIngredient(ls.getString("ing_name"));
            newIng.setQuantity(ls.getString("quantity"));
            newIng.setId(ls.getInt("ing_id"));
            ingredients.add(newIng);
//                    ingredients.add(new Ingredient(ls.getInt("ing_id"),ls.getString("ing_name"),ls.getString("quantity")));
        }
        return ingredients;
    }


    public int emailExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return 1;
            else
                return 0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int verifyLogin(String email, String pass) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if(rs.getString("user_password").equals(pass)) {
                    return rs.getInt("user_id");
                }
                else {
                    return 0; //wrong pass
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1; //wrong email
    }


    public int saveUser(User user) {
        String query = "INSERT INTO users (user_name, email, user_password) values (?,?,?)";
        try (PreparedStatement stmt = dbconn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    return userId;
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public String getUserNameFromId(int id) {
        String query = "SELECT * From users WHERE user_id = ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_name");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public int checkIfUserHasReviews(int userId, int recId) {
        String query = "SELECT * From reviews WHERE user_id = ? and rec_id = ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return 1;
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void deleteRecipe(int recId) {
        String query = "DELETE From recipe_ingredients WHERE rec_id = ?";
        String query2 = "DELETE From recipes_category WHERE rec_id = ?";
        String query3 = "DELETE From reviews WHERE rec_id = ?";
        String query4 = "DELETE From favorites WHERE rec_id = ?";
        String query5 = "DELETE From recipes WHERE rec_id = ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)){
            stmt.setInt(1, recId);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement stmt = dbconn.prepareStatement(query2)){
            stmt.setInt(1, recId);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement stmt = dbconn.prepareStatement(query3)){
            stmt.setInt(1, recId);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement stmt = dbconn.prepareStatement(query4)){
            stmt.setInt(1, recId);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement stmt = dbconn.prepareStatement(query5)){
            stmt.setInt(1, recId);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int addRecToFavorites(int rec_id, int user_id) {
        String q = "SELECT * from favorites WHERE user_id = ? and rec_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = dbconn.prepareStatement(q);
            stmt.setInt(1, user_id);
            stmt.setInt(2, rec_id);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                //removing from favorites
                String query = "DELETE FROM favorites WHERE user_id = ? and rec_id = ?";
                try (PreparedStatement stmt2 = dbconn.prepareStatement(query)) {
                    stmt2.setInt(1, user_id);
                    stmt2.setInt(2, rec_id);
                    int rowsDeleted = stmt2.executeUpdate();
                    if (rowsDeleted > 0) {
                        //removed from favs successfully
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return 0;
            }
            else {
                String query = "INSERT INTO favorites (user_id, rec_id) values (?,?)";
                try (PreparedStatement stmt2 = dbconn.prepareStatement(query)) {
                    stmt2.setInt(1, user_id);
                    stmt2.setInt(2, rec_id);
                    int rowsInserted = stmt2.executeUpdate();
                    if (rowsInserted > 0) {
                        //added to favs successfully
                        return 1;
                    }
                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public ArrayList<Recipe> searchByIngredient(String keyword) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();

        String query = "SELECT DISTINCT r.* FROM recipe_ingredients ri, ingredients i, recipes r WHERE ri.ing_id = i.ing_id AND r.rec_id = ri.rec_id AND i.ing_name LIKE ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setString(1, "%"+keyword+"%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));
                recipe.setRec_id(rs.getInt("rec_id"));
                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();
                File f = File.createTempFile("image_file", ".jpg");
                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));
                ArrayList<String> categories = new ArrayList<>();
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                recipe.setIngredients(ingredients);
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return recipes;
    }

    public ArrayList<Recipe> searchByName(String keyword) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();

        int v = 0;
        String query = "SELECT DISTINCT * FROM recipes WHERE rec_name LIKE ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                v++;
                Recipe recipe = new Recipe();
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));
                recipe.setRec_id(rs.getInt("rec_id"));
                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();

                //added
                File f = new File("image_file" + v + ".jpg");

                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));
                ArrayList<String> categories = new ArrayList<>();
                //
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                //
                recipe.setIngredients(ingredients);
                // Add the recipe object to the list
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }
    public ArrayList<Recipe> searchByCategory(String keyword) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();

        String query = "SELECT DISTINCT r.* FROM recipes_category ri, categories i, recipes r WHERE ri.cat_id = i.cat_id AND r.rec_id = ri.rec_id AND i.cat_name LIKE ?";
        try (PreparedStatement stmt = dbconn.prepareStatement(query)) {
            stmt.setString(1, "%"+keyword+"%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setName(rs.getString("rec_name"));
                recipe.setInstructions(rs.getString("steps"));
                recipe.setComment(rs.getString("comments"));
                recipe.setRec_id(rs.getInt("rec_id"));
                recipe.setReviews_count(rs.getInt("rev_count"));
                recipe.setAverage(rs.getFloat("average"));
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();
                File f = File.createTempFile("image_file", ".jpg");
                OutputStream outputStream = new FileOutputStream(f);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                recipe.setImage(f);
                inputStream.close();
                outputStream.close();
                recipe.setServing(rs.getInt("servings"));
                recipe.setUser_id(rs.getInt("author_id"));
                recipe.setCuisine(rs.getString("cuisine"));
                recipe.setCook_time(rs.getString("cooking_time"));
                recipe.setAuthor_name(getUserNameFromId(recipe.getUser_id()));
                ArrayList<String> categories = new ArrayList<>();
                categories = getAllCatgoriesForRecipe(rs.getInt("rec_id"));
                recipe.setCategories(categories);
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ingredients = getAllIngredientsForRecipe(rs.getInt("rec_id"));
                recipe.setIngredients(ingredients);
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return recipes;
    }
}


