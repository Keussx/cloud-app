package myrecipes.app.entities.endpoint.response;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AvailableAllergens {
@Id
    private String allergens;

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }
}
