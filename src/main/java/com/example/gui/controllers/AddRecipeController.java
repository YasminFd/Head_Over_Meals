package com.example.gui.controllers;

import com.example.gui.DBConnection;
import com.example.gui.builder.Director;
import com.example.gui.builder.MyRecipesBuilder;
import com.example.gui.builder.RecipesPageBuilder;
import com.example.gui.flyweight.IngredientFactory;
import com.example.gui.models.Ingredient;
import com.example.gui.models.Recipe;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddRecipeController {

    private int user_id;
    @FXML
    private HeaderController headerController;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        headerController.setUser_id(user_id);
        this.user_id = user_id;
    }

    private Recipe r=null;

    public void setR(Recipe r) {
        this.r = r;
    }
    private int editing = 0;
    private DBConnection z = DBConnection.getInstance();



    private ArrayList<HBox> ing = new ArrayList<>();
    private ArrayList<TextField> categ = new ArrayList<>();
    private ArrayList<HBox> instructs = new ArrayList<>();
    private Button remove_category=new Button("-"), remove_step=new Button("-"),remove_ingredient=new Button("-");
    private int counter=0, image=0;
    private File selectedFile;
    private String imagePath="";
    private ImageView photo;
    @FXML
    private Button add_image;
    @FXML
    private TextField recipe_name,cuisine_name,cook_time,serving_number;
    @FXML
    private TextArea comment;
    @FXML
    private VBox steps,ingredients, images;
    @FXML
    private HBox categories;
    @FXML
    private Label error_type, error;
    @FXML
    private Button confirm;

    public void initialize() {
        Platform.runLater(() ->  {


                    if(r!=null){//we are editing
                        editing = 1;
                        //add name
                        recipe_name.setText(r.getName());

                        //add cuisine
                        cuisine_name.setText(r.getCuisine());

                        //add serving
                        serving_number.setText(r.getServing()+"");

                        //add cook time

                        cook_time.setText(r.getCook_time());

                        //addcomment
                        comment.setText(r.getComment());

                        //button
                        confirm.setText("Edit Recipe");

                        //add Steps

                        String ins = r.getInstructions();
                        String [] s = ins.split("\n");
                        int i=1;
                        remove_step.setPrefWidth(30);
                        remove_step.setPrefHeight(26);
                        remove_step.setStyle("-fx-background-color: FF6468");
                        remove_step.setTextFill(Color.WHITE);
                        if (!steps.getChildren().contains(remove_step)){
                            steps.getChildren().add(remove_step);
                        }
                       // steps.getChildren().add(remove_step);
                        for(String a:s){
                            Label nb = new Label();
                            nb.setText(i+"");
                            nb.setPrefWidth(30);
                            TextArea inst = new TextArea();
                            inst.setText(a);
                            inst.setPrefWidth(600);
                            inst.setPrefHeight(200);
                            inst.setWrapText(true);
                            HBox step = new HBox(nb,inst);
                            instructs.add(step);
                            steps.getChildren().add(step);
                            i++;
                        }
                        counter=i;

                        //add ingredients
                        i=0;
                        remove_ingredient.setPrefWidth(30);
                        remove_ingredient.setPrefHeight(26);
                        remove_ingredient.setStyle("-fx-background-color: FF6468");
                        remove_ingredient.setTextFill(Color.WHITE);
                        if (!ingredients.getChildren().contains(remove_ingredient)){
                            ingredients.getChildren().add(remove_ingredient);
                        }
                        //ingredients.getChildren().add(remove_ingredient);
                        ArrayList<Ingredient> ings= r.getIngredients();
                        for(Ingredient a:ings){
                            TextField name = new TextField();
                            name.setPrefWidth(530);
                            name.setText(a.getIngredient());
                            TextField quantity = new TextField();
                            quantity.setText(a.getQuantity());
                            HBox ingredient= new HBox(name,quantity);
                            ingredient.setSpacing(50);
                            ingredients.getChildren().add(ingredient);
                            ing.add(ingredient);
                        }

                        //add categories
                        i=0;
                        remove_category.setPrefWidth(30);
                        remove_category.setPrefHeight(26);
                        remove_category.setStyle("-fx-background-color: FF6468");
                        remove_category.setTextFill(Color.WHITE);
                        if (!categories.getChildren().contains(remove_category)){
                            categories.getChildren().add(remove_category);
                        }
                        //categories.getChildren().add(remove_category);
                        ArrayList<String> cats = r.getCategories();
                        for(String a:cats){
                            TextField cat = new TextField();
                            cat.setText(a);
                            cat.setPrefWidth(200);
                            categories.getChildren().add(cat);
                            categ.add(cat);
                            //categories.layout();
                        }

                        //add image
                        try {
                            if(r.getImage()!= null) {
                                InputStream f = new FileInputStream(r.getImage());
                                Image im = new Image(f);
                                photo = new ImageView();
                                photo.setImage(im);
                                photo.setFitHeight(300);
                                photo.setFitWidth(400);
                                images.getChildren().add(photo);
                                image = 1;
                                add_image.setText("-");
                                selectedFile = r.getImage();
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }remove_ingredient.setOnAction(event -> {
                        if(ing.size()>0) {
                            ingredients.getChildren().remove(ing.get(ing.size() - 1));
                            //ing_info.remove(ing_info.size()-1);
                            ing.remove(ing.size() - 1);
                            if (ing.size() == 0)
                                ingredients.getChildren().remove(remove_ingredient);
                        }
                    });

                    remove_step.setOnAction(event -> {
                        if(instructs.size()>0) {
                            steps.getChildren().remove(instructs.get(instructs.size()-1));
                            instructs.remove(instructs.size()-1);
                            counter--;
                            if(instructs.size()==0)
                                steps.getChildren().remove(remove_step);}
                    });
                    remove_category.setOnAction(event -> {
                        if(categ.size()>0){
                            categories.getChildren().remove(categ.get(categ.size()-1));
                            categ.remove(categ.size()-1);
                            if(categ.size()==0)
                                categories.getChildren().remove(remove_category);}
                    });

                }

        );}
    @FXML
    protected void CheckNumber(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9]")) {
            error_type.setText("Input an integer");
            event.consume();
        }
        else{
            error_type.setText("");
        }
    }
    @FXML
    protected void AddIngredient(ActionEvent actionEvent){
        if(ing.size()==0 && !ingredients.getChildren().contains(remove_ingredient))
        {
            remove_ingredient.setPrefWidth(30);
            remove_ingredient.setPrefHeight(26);
            remove_ingredient.setStyle("-fx-background-color: FF6468");
            remove_ingredient.setTextFill(Color.WHITE);
            ingredients.getChildren().add(remove_ingredient);
        }
        TextField name = new TextField();
        name.setPrefWidth(530);
        TextField quantity = new TextField();
        HBox ingredient= new HBox(name,quantity);
        ingredient.setSpacing(50);
        ingredients.getChildren().add(ingredient);
        ing.add(ingredient);


    }

    @FXML
    protected void AddStep(ActionEvent actionEvent){
        if(counter==0 && !steps.getChildren().contains(remove_step))
        {
            remove_step.setPrefWidth(30);
            remove_step.setPrefHeight(26);
            remove_step.setStyle("-fx-background-color: FF6468");
            remove_step.setTextFill(Color.WHITE);
            steps.getChildren().add(remove_step);
        }

        counter++;
        Label nb = new Label();
        nb.setText(counter+"");
        nb.setPrefWidth(30);
        TextArea inst = new TextArea();
        inst.setPrefWidth(600);
        inst.setPrefHeight(100);
        inst.setWrapText(true);
        HBox step = new HBox(nb,inst);
        instructs.add(step);
        steps.getChildren().add(step);
    }

    @FXML
    protected void AddCategory(ActionEvent actionEvent){

        if(categ.size()<8 ){
            if(categ.size()==0 && !categories.getChildren().contains(remove_category))
            {
                remove_category.setPrefWidth(30);
                remove_category.setPrefHeight(26);
                remove_category.setStyle("-fx-background-color: FF6468");
                remove_category.setTextFill(Color.WHITE);
                categories.getChildren().add(remove_category);
            }

            TextField cat = new TextField();
            categories.getChildren().add(cat);
            categ.add(cat);
        }

    }
    @FXML
    protected void AddImage(ActionEvent actionEvent) throws IOException, URISyntaxException {
        if(image==0){
            image=1;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );


            // Show the FileChooser and get the selected file
            selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                imagePath = selectedFile.getPath();
                photo = new ImageView(imagePath);
                photo.setFitHeight(300);
                photo.setFitWidth(400);
                images.getChildren().add(photo);
                image++;
                add_image.setText("-");
            }

        }
        else{
            images.getChildren().remove(photo);
            image = 0;
            add_image.setText("+");
        }



    }
    @FXML
    protected void AddRecipe(ActionEvent actionEvent) throws IOException, SQLException {
        ArrayList<Ingredient> ing_info = new ArrayList<>();
        ArrayList<String> categ_info = new ArrayList<>();
        String steps_info="";

        if (recipe_name.getText().toString().equals("")) {
            error.setText("plz enter recipe name");
            return;
        }
        if (cuisine_name.getText().toString().equals("")) {
            error.setText("plz enter cuisine name");
            return;
        }
        if (serving_number.getText().toString().equals("")) {
            error.setText("plz enter serving number");
            return;
        }
        if (cook_time.getText().toString().equals("")) {
            error.setText("plz enter cook time");
            return;
        }
        if (ing.isEmpty()) {
            error.setText("plz enter at least enter one ingredient");
            return;
        }
        if (categ.isEmpty()) {
            error.setText("plz enter at least enter one category");
            return;
        }
        if (instructs.isEmpty()) {
            error.setText("plz enter at least enter one instruction");
            return;
        }
        for (HBox item : ing) {
            TextField name = (TextField) item.getChildren().get(0);
            String a = name.getText().toString();
            TextField quantity = (TextField) item.getChildren().get(1);
            String b = quantity.getText().toString();
            if (a.equals("") || b.equals("")) {
                error.setText("No empty fields are allowed");
                return;
            }
            Ingredient ing_new = IngredientFactory.getIngredient(a);
            ing_new.setQuantity(b);
            ing_info.add(ing_new);
        }
        for (TextField item : categ) {
            String a = item.getText().toString();
            if (a.equals("")) {
                error.setText("No empty fields are allowed");
                return;
            }
            categ_info.add(a);
        }
        for (HBox item : instructs) {
            TextArea step = (TextArea) item.getChildren().get(1);
            String a = step.getText().toString();
            if (a.equals("")) {
                error.setText("No empty fields are allowed");
                return;
            }
            String newStr = a.replaceAll("\\n", " ");
            steps_info += (newStr + "\n");
        }
        if (selectedFile == null) {
            error.setText("plz upload an image");
            return;
        }
        DBConnection c = DBConnection.getInstance();
        if (editing == 0) {
            r= new Recipe();
            r.setName(recipe_name.getText().toString());
            r.setCuisine(cuisine_name.getText().toString());
            r.setCook_time(cook_time.getText().toString());
            r.setServing(Integer.parseInt(serving_number.getText()));
            r.setUser_id(this.getUser_id());
            r.setIngredients(ing_info);
            r.setCategories(categ_info);
            r.setComment(comment.getText().toString());
            r.setImage(selectedFile);
            r.setInstructions(steps_info);
            r.setAuthor_name(z.getUserNameFromId(user_id));
            //r = new Recipe(recipe_name.getText().toString(), cuisine_name.getText().toString(), cook_time.getText().toString(), Integer.parseInt(serving_number.getText().toString()), this.getUser_id(),z.getUserNameFromId(user_id), ing_info, categ_info, steps_info, comment.getText().toString(), selectedFile);
            int id = z.saveRecipe(r);
            r.setRec_id(id);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui/RecipeView.fxml"));
                Parent root = loader.load();
                ViewRecipeController controller = loader.getController();
                controller.setUser_id(this.user_id);
                controller.setX(r);
                Scene scene = add_image.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            r.setName(recipe_name.getText().toString());
            r.setCuisine(cuisine_name.getText().toString());
            r.setCook_time(cook_time.getText().toString());
            r.setServing(Integer.parseInt(serving_number.getText()));
            r.setUser_id(this.getUser_id());
            r.setIngredients(ing_info);
            r.setCategories(categ_info);
            r.setComment(comment.getText().toString());
            r.setImage(selectedFile);
            r.setInstructions(steps_info);
            r.setAuthor_name(z.getUserNameFromId(user_id));
            //r = new Recipe(recipe_name.getText().toString(), cuisine_name.getText().toString(), cook_time.getText().toString(), Integer.parseInt(serving_number.getText().toString()), this.getUser_id(),z.getUserNameFromId(user_id), ing_info, categ_info, steps_info, comment.getText().toString(), selectedFile);
            z.updateRecipe(r);
            Director d = new Director();
            RecipesPageBuilder b = new MyRecipesBuilder(add_image, user_id);
            d.setBuilder(b);
            d.ConstructPage();

        }
    }



}