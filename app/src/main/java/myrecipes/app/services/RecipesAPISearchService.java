package myrecipes.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import myrecipes.app.entities.datastore.Recipe;
import myrecipes.app.util.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RecipesAPISearchService {

	private static final String YUMMLY_API_KEY = "b03bfa47130539a642d0c969cf6ac7f7";

	private static final String YUMMLY_API_ID = "3125ae78";

	private JsonFactory jsonFactory;

	private IngredientsGeneratorWorker ingredientsGeneratorWorker;

	private RecipeDatastoreWorker recipeDatastoreWorker;

	public RecipesAPISearchService(){
		jsonFactory = new JsonFactory();
		ingredientsGeneratorWorker = new IngredientsGeneratorWorker();
		recipeDatastoreWorker = new RecipeDatastoreWorker();
	}
	

	//search for recipes using ingredients
	public List<Recipe> search(String ingredientsJSONArray, String allergensJSONArray, String cuisinesJSONArray){

		//simple get request
		String request = "http://api.yummly.com/v1/api/recipes?_app_id="+YUMMLY_API_ID+"&_app_key="+YUMMLY_API_KEY;
		List<Recipe> list = new ArrayList<>();

		//add the ingredients to request if correct type of data was received
		if(ingredientsJSONArray!=null && !ingredientsJSONArray.equals("")){

			List<String> ingredients = jsonFactory.toList(ingredientsJSONArray);
			if (ingredients != null && ingredients.size()>0){
				//add all ingredients to request
				try{
					for (String ig : ingredients)
						request += "&allowedIngredient[]=" + URLEncoder.encode(ig, StandardCharsets.UTF_8.toString());
				}catch(Exception p){}
			}
		}

		//add the allergens to request if correct type of data was received
		if(allergensJSONArray!=null && !allergensJSONArray.equals("")){
			List<String>allergens = jsonFactory.toList(allergensJSONArray);
			if (allergens != null && allergens.size()>0){
				try {
					//add all allergens to request
					for (String al : allergens) {
						String searchcode = ingredientsGeneratorWorker.getAllergenSearchCode(al);

						if (searchcode != null && !searchcode.equals(""))
							request += "&allowedAllergy[]=" + URLEncoder.encode(searchcode, StandardCharsets.UTF_8.toString());
					}
				}catch (Exception e){}

			}
		}



		//add the cuisines to request if correct type of data was received
		if(cuisinesJSONArray!=null && !cuisinesJSONArray.equals("")){
			List<String> cuisines = jsonFactory.toList(cuisinesJSONArray);
			if (cuisines != null && cuisines.size()>0){
				//add all cuisines to request
				try {
					for (String cu : cuisines)
						request += "&allowedCuisine[]=" + URLEncoder.encode("cuisine^cuisine-" + cu, StandardCharsets.UTF_8.toString());
					;
				}catch (Exception e){}
			}
		}


		try {

			URL obj = new URL(request);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//create and jsonnode object that maps to the root of the json string node
			ObjectMapper objectMapper = new ObjectMapper();
			//objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode jsonNodeRoot = objectMapper.readTree(response.toString());
			JsonNode matches = jsonNodeRoot.get("matches");

			//iterate through all the recipe nodes
			for(int index=0;index <matches.size();index++){

				//retrieve details
				JsonNode recipe = matches.get(index);
				String recipeID = rmQuotes(recipe.get("id").textValue());
				String recipeName = rmQuotes(recipe.get("recipeName").textValue());
				String prepTimeSeconds = rmQuotes(recipe.get("totalTimeInSeconds").asInt() + "");
				String img = rmQuotes(recipe.get("smallImageUrls").get(0).textValue());
				String instructions = "https://www.yummly.co.uk/#recipe/"+ recipeID;
				String ingredients =  rmQuotes(recipe.get("ingredients").toString());

				//try to add the recipe to the datastore if not exist
				recipeDatastoreWorker.addRecipe(recipeID, recipeName, prepTimeSeconds, img, ingredients, instructions);

				//to to get the recipe and print
				Recipe r = recipeDatastoreWorker.getRecipe(recipeID);

				if (r != null)
					list.add(r);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	
	}

	//method removes quotes from string
	private String rmQuotes(String s){
		return s.replaceAll("^\"|\"$", "");
	}

}
