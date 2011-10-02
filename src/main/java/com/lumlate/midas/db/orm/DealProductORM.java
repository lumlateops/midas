package com.lumlate.midas.db.orm;

public class DealProductORM {

	private long id;
	private long DealId;
	private long ProductId;
	private String tags;

	public DealProductORM(long id, long dealId, long productId, String tags) {
		super();
		this.id = id;
		DealId = dealId;
		ProductId = productId;
		this.tags = tags;
	}

	public DealProductORM() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDealId() {
		return DealId;
	}

	public void setDealId(long dealId) {
		DealId = dealId;
	}

	public long getProductId() {
		return ProductId;
	}

	public void setProductId(long productId) {
		ProductId = productId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void clear() {
		id = 0;
		DealId = 0;
		ProductId = 0;
		tags = null;

	}
}
