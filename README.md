# Recipe-manager
The objective of this project is to allow a user to be able to manage his/her recipes by retrieving, adding, updating and deleting them.

In addition a user can also filter the recipes by:

* number of servings
* if the recipe is vegetarian or not
* a word match in the instructions
* without certain ingredient
* with certain ingredient

### Running the app
You can run the application by running the file `RecipemanagerApplication.java`. the service will be run at the url **http://localhost:8080/recipes**. For more documentation about the endpoint you can access the [localhost swagger page](http://localhost:8080/swagger-ui/index.html). You can also use the imported collection from Postman in the root folder with the endpoints ready to be used. 

### Architectural Decisions

* Due to time limits for the implementation there where bottlenecks that had to be avoided. First pagination would have been to expensive timewise to implement considering that results will have to be filtered, implementation of the offset per page would have to be done and it would have taken much more time.
* A persistent H2 Database that writes to a file was the way chosen to go as the application is meant to be a standalone for a user. The easy and quick configuration was worth the use.
* That said, the importance of filters running smoothly was important so they have been ordered in a way to have minor efficiency cost possible. that is why if the user decides to combine them they have been set in a way that the most efficient operations are the last to be done because then amount of recipes that have to be taken into account are less.
* Since the time was so short it was decided to use `lombok` and `modelMapper` to remove boilerplate code.
* Validation has been done both with `spring-validators` to once again to be quicker and also there are custom exceptions to add more information for the user.
* Extra logic was added for ingredient naming to remove spaces before and after the words, also they are changed to lowercase, this would make it easier to find if the same ingredient already exists in the database and also easier to use as a parameter in the search recipes endpoint.
* Updating the recipe can only be done fully by using **PUT**. **PATCH** and a partial update would have taken more time to develop.
* At a later stage, having used modelMapper was detrimental when creating tests that is why it was decided to make the application as safe as possible eventhough some tests have been developed.

