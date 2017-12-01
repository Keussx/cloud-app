package myrecipes.app.services;

import java.util.ArrayList;
import java.util.List;
import com.google.cloud.datastore.*;
import myrecipes.app.entities.datastore.Recipe;
import myrecipes.app.entities.endpoint.response.RecipesSaved;
import myrecipes.app.util.JsonFactory;

public class RecipeDatastoreWorker {

	// Create an authorized Datastore service
	//private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	

	private final Datastore datastore = DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("ohmyrecipes").build().getService();

	// Create a Key factory to handle keys for Recipe kind
	private final KeyFactory recipeKeyFactory = datastore.newKeyFactory().setKind("Recipe");

	// Create a Key factory to handle keys for UserRecipe kind
	private final KeyFactory userRecipeKeyFactory = datastore.newKeyFactory().setKind("UserRecipe");

	// Create a Key factory to handle keys for User kind
	private final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");



	/**
	 * Inserts a Recipe to datastore
	 *
	 * @params all recipe details and user id
	 */

	public void addRecipe(String id, String name, String prepTime, String imgUrl, String ingredients, String instructions) {

		/**INSERTING THE RECIPE**/

		// create a key for the recipe entity
		IncompleteKey key = recipeKeyFactory.newKey(id);

		FullEntity<IncompleteKey> fullEntity = Entity.newBuilder(key).set(Recipe.ID, id)
				.set(Recipe.NAME, name).set(Recipe.INSTRUCTIONS, instructions)
				.set(Recipe.PREPTIME, prepTime).set(Recipe.IMGURL,imgUrl)
				.set(Recipe.INGREDIENTS, ingredients).build();

		// use put to save the recipe to the datastore, so that it insert only is not exist
		Entity entity = datastore.put(fullEntity);

	}

	/**
	 * Inserts a Recipe to datastore
	 *
	 * @param recipe a recipe to store
	 */

	public void addRecipe(Recipe recipe) {

		/**INSERTING THE RECIPE**/

		addRecipe(recipe.getId(), recipe.getName(), recipe.getInstructions(), recipe.getPrepTime(), recipe.getImgUrl(), recipe.getIngredients());

	}

	/**
	 * Saves a Recipe for a user to datastore
	 *
	 * @param recipeIdsJSON a json string containing recipe ids
	 * @param userId a user id
	 */

	public RecipesSaved addUserRecipe(String recipeIdsJSON, String userId) {

		JsonFactory jsonFactory = new JsonFactory();
		List<String> recipeIds = new ArrayList<>();
		RecipesSaved rs = new RecipesSaved();
		rs.setStatus("ok");

		//save the recipeIds if correct type of data was received
		if(recipeIdsJSON!=null && !recipeIdsJSON.equals("")){
			List<String> temp = jsonFactory.toList(recipeIdsJSON);
			if (temp != null && temp.size()>=1){

				try {
					//add all allergens to request
					for (String al : temp) {
						recipeIds.add(al);
					}
				}catch (Exception e){}

			}

		}

		//check if the ids are valid and insert them for the user
		//if there is no allergens get all allergens
		if(recipeIds.size()>0){


			for(String recipeid: recipeIds){

				System.out.println(">> " + recipeid + " >> " + userId);

				//get the key for the recipe id
				Key recipeKey = recipeKeyFactory.newKey(recipeid);

				//get the key for the user id
				Key userKey = userKeyFactory.newKey(userId);

				//check if both keys are valid
				try{
					datastore.get(recipeKey).getString("name");
					datastore.get(userKey).getString("name");

					//insert to ingredient kind
					Entity entity =  Entity.newBuilder(userRecipeKeyFactory.newKey(userId + recipeid)).set("user", userKey).set("recipe", recipeKey).build();
					datastore.put(entity);

				}catch(Exception e){
					//failure
					e.printStackTrace();
					rs.setStatus("error: failed to save");
				}
			}
		}
		else{
			rs.setStatus("error: nothing to save");
		}

		return rs;
	}


	/**
	 * Retrieves a recipe from datastore using its ID
	 * 
	 * @param recipeId of id of recipe
	 * @return recipe instance
	 */
	public Recipe getRecipe(String recipeId) {
		//create a key using both ids
		Key key = recipeKeyFactory.newKey(recipeId);

		//get the recipe
		try {
			//get recipe
			Entity recipe =  datastore.get(key);

			//means the record exists
			if(recipe != null) {
				return convertEntityToRecipe(recipe);
			}

		}catch (Exception e){}
		return null;
	}

	/**
	 * Retrieves all recipes from datastore saved by a user
	 *
	 * @return list of recipes
	 */
	public List<Recipe> getAllUserRecipes(String userId) {

		//to return as result
		List<Recipe> userRecipes = new ArrayList<>();

		//get the key for the user id
		Key userKey = userKeyFactory.newKey(userId);

		//check if both keys are valid
		try {
			//checks if entity exists, by getting its id back		}
			String id = datastore.get(userKey).getString("name");

			Query<Entity> query = Query.newEntityQueryBuilder()
					.setFilter(StructuredQuery.PropertyFilter.eq("user", userKey))
					.setKind("UserRecipe").build();

			QueryResults<Entity> results = datastore.run(query);

			while (results.hasNext()) {
				//get entity
				Entity e = results.next();

				try {
					Entity recipe = datastore.get(e.getKey("recipe"));

					userRecipes.add(convertEntityToRecipe(recipe));

				} catch (NullPointerException x) {	}
			}

		}catch(Exception e){

		}
		return userRecipes;
	}
	
	/**
	 * Convert an Entity to a recipe instance
	 * 
	 * @param entity object
	 * @return resulted recipe
	 */
	public Recipe convertEntityToRecipe(Entity entity) {
		
		//create a recipe instance using entity values
		Recipe recipe = new Recipe.Builder()
				.id(entity.getString(Recipe.ID))
		        .name(entity.getString(Recipe.NAME))
		        .instructions(entity.getString(Recipe.INSTRUCTIONS))
		        .prepTime(entity.getString(Recipe.PREPTIME))
		        .imgUrl(entity.getString(Recipe.IMGURL))
				.ingredients(entity.getString(Recipe.INGREDIENTS))
		        .build();
		
		// return the recipe
		return recipe;
	}

}
