package myrecipes.app.entities.endpoint.response;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AvailableIngredients {

    @Id
    // a json string containing recipe id's selected by the user
    private List<String> jsonIngredients;

    public AvailableIngredients(){
        jsonIngredients = new ArrayList<>();
    }

    public List<String> getIngredients() {
        return jsonIngredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.jsonIngredients = ingredients;
    }

    public void addIngredient(String ingredient){
        jsonIngredients.add(ingredient);
    }
}
