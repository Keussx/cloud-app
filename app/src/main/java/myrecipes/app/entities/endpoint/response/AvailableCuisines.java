package myrecipes.app.entities.endpoint.response;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AvailableCuisines {
@Id
    private String cuisines;

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }
}
