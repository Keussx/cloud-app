package myrecipes.app.services;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import myrecipes.app.entities.endpoint.response.AvailableAllergens;
import myrecipes.app.entities.endpoint.response.AvailableCuisines;
import myrecipes.app.entities.endpoint.response.AvailableIngredients;
import myrecipes.app.entities.endpoint.response.Ingredient;
import myrecipes.app.util.JsonFactory;
import java.util.ArrayList;
import java.util.List;


public class IngredientsGeneratorWorker {

    //private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private Datastore datastore = DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("ohmyrecipes").build().getService();

    private KeyFactory allergKeyFactory = datastore.newKeyFactory().setKind("Allergen");

    private KeyFactory cuiKeyFactory = datastore.newKeyFactory().setKind("Cuisine");

    private KeyFactory ingrKeyFactory = datastore.newKeyFactory().setKind("Ingredient");

    private KeyFactory ingAlKeyFactory = datastore.newKeyFactory().setKind("IngredientAllergen");

    public AvailableCuisines getAllCuisines(){

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Cuisine")
                .setOrderBy(OrderBy.asc("name"))
                .build();

        QueryResults<Entity> results = datastore.run(query);
        List<String> values = new ArrayList<String>();

        while(results.hasNext()){
            Entity e = results.next();
            values.add(e.getString("name"));
        }
        AvailableCuisines av = new AvailableCuisines();
        //convert list to json, add to entity and return
        av.setCuisines(new JsonFactory().toJSon(values));
        return av;
    }

    public AvailableAllergens getAllAllergens(){

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Allergen")
                .setOrderBy(OrderBy.asc("name"))
                .build();

        QueryResults<Entity> results = datastore.run(query);
        List<String> values = new ArrayList<String>();

        while(results.hasNext()){
            Entity e = results.next();
            values.add(e.getString("name"));
        }

        AvailableAllergens av = new AvailableAllergens();

        //convert list to json, add to entity and return
        av.setAllergens(new JsonFactory().toJSon(values));
        return av;
    }

    public String getAllergenSearchCode(String allergen){

        Key key =allergKeyFactory.newKey(allergen);

        if(key != null)
            try {
                return datastore.get(key).getString("searchcode");
            }catch (Exception e){}

        return null;
    }

    public AvailableIngredients getAllIngredients(){

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Ingredient")
                .setOrderBy(OrderBy.asc("name"))
                .build();

        QueryResults<Entity> results = datastore.run(query);
        AvailableIngredients av = new AvailableIngredients();

        JsonFactory j = new JsonFactory();

        while(results.hasNext()) {
            Entity e = results.next();
            Ingredient i = new Ingredient(e.getString("name"), e.getString("type"));
            av.addIngredient(j.toJSon(i));
        }

        //convert list to json, add to entity and return
        return av;

    }

    public AvailableIngredients getIngredients(String allergensJSONArray, String cuisinesJSONArray){

        JsonFactory jsonFactory = new JsonFactory();
        List<Key> ingredients = new ArrayList<>();
        List<String> allergens = new ArrayList<>();

        //only allergens will be used, cuisines can be added in future

        //save the allergens if correct type of data was received
        if(allergensJSONArray!=null && !allergensJSONArray.equals("")){
            List<String> temp = jsonFactory.toList(allergensJSONArray);
            if (temp != null && temp.size()>0){

                try {
                    //add all allergens to request
                    for (String al : temp) {
                        allergens.add(al);
                    }
                }catch (Exception e){}

            }

        }

        //if there is no allergens get all allergens
        if(allergens.size()==0){
                return getAllIngredients();
        }
        else {
            AvailableIngredients av = new AvailableIngredients();

            Query<Entity> query = Query.newEntityQueryBuilder()
                    .setKind("IngredientAllergen").build();

            QueryResults<Entity> results = datastore.run(query);

            while (results.hasNext()) {
                //get entity
                Entity e = results.next();

                try {
                    Entity allergen = datastore.get(e.getKey("allergen"));

                    if (allergens.contains(allergen.getString("name"))) {
                        ingredients.add(e.getKey("ingredient"));
                    }
                } catch (Exception x) {
                }
            }

            Query<Entity> query2 = Query.newEntityQueryBuilder()
                    .setOrderBy(OrderBy.asc("name"))
                    .setKind("Ingredient").build();

            QueryResults<Entity> results2 = datastore.run(query2);



            JsonFactory j = new JsonFactory();

            while (results2.hasNext()) {
                Entity k = results2.next();

                if (!ingredients.contains(k.getKey())) {
                    //save the ingredient
                    Ingredient i = new Ingredient(k.getString("name"), k.getString("type"));
                    av.addIngredient(j.toJSon(i));
                }

            }
            return av;
        }

    }
}
