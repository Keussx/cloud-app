package myrecipes.app.entities.datastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Recipe {
	// variables for holding attribute values
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String id;
	@Persistent
	private String name;
	@Persistent
	private String prepTime;
	@Persistent
	private String imgUrl;
	@Persistent
	private String ingredients;
	@Persistent
	private String instructions;


	// entity attribute names
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PREPTIME = "prepTime";
	public static final String IMGURL = "imgUrl";
	public static final String INGREDIENTS = "ingredients";
	public static final String INSTRUCTIONS = "instructions";


	public Recipe(Builder builder) {
		this.setId(builder.id);
		this.setName(builder.name);
		this.setInstructions(builder.instructions);
		this.setPrepTime(builder.prepTime);
		this.setIngredients(builder.ingredients);
		this.setImgUrl(builder.imgUrl);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}




	public static class Builder {
		private String id;
		private String name;
		private String prepTime;
		private String imgUrl;
		private String ingredients;
		private String instructions;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder imgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
			return this;
		}

		public Builder prepTime(String prepTime) {
			this.prepTime = prepTime;
			return this;
		}

		public Builder ingredients(String ingredients) {
			this.ingredients = ingredients;
			return this;
		}

		public Builder instructions(String instructions) {
			this.instructions = instructions;
			return this;
		}

		public Recipe build() {
			return new Recipe(this);
		}
	}

}
