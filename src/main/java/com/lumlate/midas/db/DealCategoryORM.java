package com.lumlate.midas.db;

public class DealCategoryORM {

	private long id;
	private String category;
	private String description;
	
	public DealCategoryORM(long id, String category, String description) {
		super();
		this.id = id;
		this.category = category;
		this.description = description;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
