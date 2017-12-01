package myrecipes.app.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import myrecipes.app.entities.datastore.Recipe;
import myrecipes.app.entities.endpoint.response.AvailableAllergens;
import myrecipes.app.entities.endpoint.response.AvailableCuisines;
import myrecipes.app.entities.endpoint.response.AvailableIngredients;
import myrecipes.app.entities.endpoint.response.RecipesSaved;
import myrecipes.app.services.IngredientsGeneratorWorker;
import myrecipes.app.services.RecipeDatastoreWorker;
import myrecipes.app.services.RecipesAPISearchService;

import java.util.List;

@Api(name = "ohmyrecipesAPI", version = "v1", description = "OhMyRecipes APi")

public class API {

	private IngredientsGeneratorWorker ingredientsGeneratorWorker = new IngredientsGeneratorWorker();

	private RecipesAPISearchService recipesAPISearchService = new RecipesAPISearchService();

	private RecipeDatastoreWorker recipeDatastoreWorker = new RecipeDatastoreWorker();


	@ApiMethod(name = "getCuisines", path = "getCuisines", description = "get all cuisines (based on cultures)",  httpMethod = HttpMethod.POST)

	public AvailableCuisines getCuisines(@Named("userId") String userId) {

		return ingredientsGeneratorWorker.getAllCuisines();
	}



	@ApiMethod(name = "getAllergens", path = "getAllergens", description = "get all types of food allergens", httpMethod = HttpMethod.POST)

	public AvailableAllergens getAllergens(@Named("userId") String userId) {

		return ingredientsGeneratorWorker.getAllAllergens();
	}



	@ApiMethod(name = "getIngredients", path = "getIngredients", description = "get custom list of ingredients", httpMethod = HttpMethod.POST)

	public AvailableIngredients getIngredients(@Named("userId") String userId, @Named("allergens") String allergens, @Named("cuisines") String cuisines) {

		return ingredientsGeneratorWorker.getIngredients(allergens, cuisines);

	}



	@ApiMethod(name = "getRecipes", path = "getRecipes", description = "get recommended recipes using ingredients, allergens and cuisine", httpMethod = HttpMethod.POST)

	public List<Recipe> getRecipes(@Named("userId") String userId, @Named("ingredients") String ingredients, @Named("allergens") String allergens, @Named("cuisines") String cuisines) {

		// return list of recipes
		return recipesAPISearchService.search(ingredients, allergens,cuisines);

	}


	@ApiMethod(name = "saveUserRecipes", path = "saveUserRecipes", description = "save recipes for a user", httpMethod = HttpMethod.POST)

	public RecipesSaved saveUserRecipes(@Named("userId") String userId, @Named("recipeIds") String recipeIds) {

		// return back results of all recipes saved
		return recipeDatastoreWorker.addUserRecipe(recipeIds, userId);
	}

	@ApiMethod(name = "getAllUserRecipes", path = "getAllUserRecipes", description = "retrieves all recipes saved by a user", httpMethod = HttpMethod.POST)

	public List<Recipe> getAllUserRecipes(@Named("userId") String userId) {

		// return back results of all recipes saved
		return recipeDatastoreWorker.getAllUserRecipes(userId);
	}

}
