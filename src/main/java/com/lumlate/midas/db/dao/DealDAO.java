package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.DealORM;

public class DealDAO {

	private long id;
	private MySQLAccess access;
	private String table = "Deal";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public DealORM insertGetId(DealORM deal) throws Exception {
		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert into "
								+ this.table
								+ " (createdAt,dealEmailId,dealRead,dealValue,discountPercentage,expiryDate,freeShipping,locationId,originalValue,postDate,title,updatedAt,url,userId,validTo,subscription_id) " +
										"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, deal.getCreatedAt());
		stmt.setLong(2, deal.getDealEmailId());
		stmt.setBoolean(3, false);
		stmt.setFloat(4, deal.getDealValue());
		stmt.setInt(5, deal.getDiscountPercentage());
		stmt.setString(6, deal.getExpiryDate());
		stmt.setBoolean(7, deal.isFreeShipping());
		stmt.setLong(8, deal.getLocationId());
		stmt.setFloat(9, deal.getOriginalValue());
		stmt.setString(10, deal.getPostDate());
		stmt.setString(11, deal.getTitle());
		stmt.setString(12, deal.getUpdatedAt());
		stmt.setString(13, deal.getUrl());
		stmt.setLong(14, deal.getUserInfoid());
		stmt.setString(15, deal.getValidTo());
		stmt.setLong(16, deal.getSubscription_id());

		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			deal.setId(generatedKeys.getLong(1));
		} else {
			throw new SQLException(
					"Creating user failed, no generated key obtained.");
		}
		// executeUpdate(query, Statement.RETURN_GENERATED_KEYS));
		return deal;
	}

}
