package myrecipes.app.startup;

import com.google.cloud.datastore.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class StartUpWorker  implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Datastore datastore = DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("ohmyrecipes").build().getService();

        KeyFactory allerKeyF = datastore.newKeyFactory().setKind("Allergen");

        KeyFactory ingKeyF = datastore.newKeyFactory().setKind("Ingredient");

        KeyFactory userKey = datastore.newKeyFactory().setKind("User");

        //do startup tasks here

        BufferedReader br = null;

        //create a User kind, and insert a default temp entity since the application is a prototype
        try{

            Entity entity =  Entity.newBuilder(userKey.newKey("temp101")).set("id", "temp101").set("name","tempuser").build();
            datastore.put(entity);

        }catch(Exception e){

        }

        //create Cuisine kind, and insert entities from csv file to datastore
        try{

            br = new BufferedReader(new FileReader(new File("src/main/java/myrecipes/app/startup/csv/cuisines.csv")));
            String line;

            KeyFactory keyF = datastore.newKeyFactory().setKind("Cuisine");

            while ((line = br.readLine()) != null) {
                Entity entity =  Entity.newBuilder(keyF.newKey(line.trim())).set("name", line.trim()).build();
                datastore.put(entity).getKey().getId();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
     

        //create Allergens kind, and insert entities from csv file to datastore
        try{

            br = new BufferedReader(new FileReader(new File("src/main/java/myrecipes/app/startup/csv/allergens.csv")));
            String line;

            while ((line = br.readLine()) != null) {
                String [] lineValues = line.split(", ");
                Entity entity =  Entity.newBuilder(allerKeyF.newKey(lineValues[0])).set("name", lineValues[0]).set("searchcode", lineValues[1]).build();
                datastore.put(entity);

            }
        }catch(Exception e){
            
        }


        //create ingredient kind, and insert entities from csv file to datastore
        try{

            br = new BufferedReader(new FileReader(new File("src/main/java/myrecipes/app/startup/csv/ingredients.csv")));
            String line;


            while ((line = br.readLine()) != null) {
                if(!line.startsWith(">")){
                    //get as array
                    String [] lineValues = line.split(", ");

                    Key ingredientKey = ingKeyF.newKey(lineValues[0]);

                    //insert to ingredient kind
                    Entity entity =  Entity.newBuilder(ingredientKey).set("name", lineValues[0]).set("type", lineValues[1]).build();
                    datastore.put(entity);

                    //if the ingredient has allergens
                    if(lineValues.length > 2){

                        for(int i=2;i<lineValues.length;i++){
                            //get the key back from database for the allergen
                            Key allergenKey =  allerKeyF.newKey(lineValues[i]);

                            KeyFactory ingAlKeyF =  datastore.newKeyFactory().setKind("IngredientAllergen")
                                    .addAncestor(PathElement.of("Allergen", lineValues[i]));


                            //insert to ingredientallergen kind
                            Entity parent =  Entity.newBuilder(ingAlKeyF.newKey(lineValues[0] + lineValues[i])).set("ingredient", ingredientKey).set("allergen", allergenKey).build();

                            //store
                            datastore.put(parent);
                        }
                    }

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
