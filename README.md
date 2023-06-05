# Head_Over_Meals

The project we present to you is Head Over Meals, a recipe management system that helps users share and view recipes throughout the application, with the option to add reviews, view ratings, collect favorite recipes or even add or edit their own.

<h5><u>Team members:</u></h4>
<ul>
<li>Fatima Dekmak</li>
<li>Yasmine Fadel</li>
<li>Dima Moukhader</li>
<li>Israa Harb</li>

Before running the application, change database credentials ( localhost , database name, username , password ) in DBConnection java class.

For a better demonstration about our application, you either run the cmd file attached with the project folder which will create the database for you, or you can manually create a database with the name homdb and import the attached sql file to it.

<h3><u>Users created in this database:</u></h3>
<ul>
<li>Bill Gates
bill@gmail.com
00000000</li>

<li>Elon Musk
elon@outlook.com
00000000</li>

<li>Mark Zuckerberg
mark@hotmail.com
00000000</li>

<li>Jeff Bezos
jeff@gmail.com
00000000</li>
  </ul>

For the given credentials, you can access recipes added to the database by these users to either edit or delete, and you can also view their favorites to add or remove from their collection of recipes.

  <h1>Views involved in our project:</h1>
6 main scenes:

  <h4><i>Home page:</i></h4>
  list of all recipes in database with search bar at the top to help filter searched ingredient or recipe name.

  <h4><i>Favorites page:</i></h4>
  list of all recipes added as favorites for the logged in user (recipe added to favorites by clicking the heart image for the recipe view).

  <h4><i>My recipes page:</i></h4> 
  list of all recipes created by the user with the option to delete or edit recipe.

  <h4><i>Recipe view page:</i></h4> 
  when clicking on the recipe, views all information related to it from name, steps, ingredients, cuisine. Categories... It also contains reviews section where it displays a form to add a new review and displays all reviews from users with sorting options.

  <h4><i>Add recipe page:</i></h4> 
  accessed by clicking add recipe button or by choosing to edit recipe from my recipes, list all textfields and text areas needed to be filled or editted to add or update recipe.

  <h4><i>Log in and sign up pages:</i></h4> 
authentication procedure to access account , without authentication you can't access the application.
<hr>
<i>All the above pages share a common header for easier navigation between these pages, containing a hyperlink for my recipes, my favorites , logout and home pages(click on logo).</i>
<hr>
  <h3><u>Design Patterns involved in the developement of this application:</u></h3>

<h4><i>Strategy Pattern for Authentication:</i></h4> 
  either use log in strategy to sign in an already existing account or sign up strategy to create a new account to use.

<h4><i>Strategy Pattern for Search and filter: </i></h4> 
  to search home page either by entering recipe name or ingredient name and decided on the filter checkbox what strategy do you intend to use

<h4><i>Flyweight pattern:</i></h4> 
  creation of ingredients where each ingredient is created once and reused in recipes having same ingredient but with setting different quantity (in our design recipes can share same ingredient name but for each recipe it sets its own needed quantity)

<h4><i>Builder pattern:</i></h4> 
  to build my recipes or my favorites page using same fxml and controller files (Recipe Page controller and file ) while hiding the direct creation logic (loading pf page and button action setting ) for each of the given types.

<h4><i>Proxy pattern: </i></h4>
 upon adding a new review, it checks the words used in the comment, if it contains a cuss word from the provided collection, it prevents its creation.

<h4><i>Observer pattern:</i></h4> 
  upon successfuly adding a new review, the rating average of the recipe is recalculated by considering the new added rating and it notifies (updates)
The average set for recipe model and the rating of recipe in recipe view for the user to see.

<h4><i>Strategy Pattern: </i></h4> 
  for sorting  reviews, either choose sort by rate strategy or by date created strategy to view resulting sorted reviews.
