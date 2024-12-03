package main.java.csye6200.models;

import java.util.UUID;

public class Category {

	private String categoryId;
	private String categoryName;
	
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId() {
		this.categoryId = UUID.randomUUID().toString();
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
	
}
