package com.yw.domain;
// Generated Oct 28, 2015 4:17:25 PM by Hibernate Tools 3.4.0.CR1

/**
 * Contains generated by hbm2java
 */
public class Contains implements java.io.Serializable {

	private Integer id;
	private Ingredients ingredients;
	private Cuisine cuisine;
	private String quantity;

	public Contains() {
	}

	public Contains(Ingredients ingredients, Cuisine cuisine, String quantity) {
		this.ingredients = ingredients;
		this.cuisine = cuisine;
		this.quantity = quantity;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Ingredients getIngredients() {
		return this.ingredients;
	}

	public void setIngredients(Ingredients ingredients) {
		this.ingredients = ingredients;
	}

	public Cuisine getCuisine() {
		return this.cuisine;
	}

	public void setCuisine(Cuisine cuisine) {
		this.cuisine = cuisine;
	}

	public String getQuantity() {
		return this.quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}