package com.lumlate.midas.db.orm;

public class DealDealCategoryORM {
	private long id;
	private long dealId;
	private long categoryId;
	
	public DealDealCategoryORM() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getDealId() {
		return dealId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setDealId(long dealId) {
		this.dealId = dealId;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
}
