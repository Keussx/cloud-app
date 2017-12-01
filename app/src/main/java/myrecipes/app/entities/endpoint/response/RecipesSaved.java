package myrecipes.app.entities.endpoint.response;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RecipesSaved {
    @Id
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
