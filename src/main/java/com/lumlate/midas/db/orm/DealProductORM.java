package com.lumlate.midas.db.orm;

public class DealProductORM {

	private long DealId;
	private long ProductsId;
	
	public DealProductORM(long dealId, long productsId) {
		super();
		DealId = dealId;
		ProductsId = productsId;
	}
	
	public long getDealId() {
		return DealId;
	}
	public void setDealId(long dealId) {
		DealId = dealId;
	}
	public long getProductsId() {
		return ProductsId;
	}
	public void setProductsId(long productsId) {
		ProductsId = productsId;
	}
	
	
	
}
