package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.SubscriptionORM;

public class SubscriptionDAO {

	private MySQLAccess access;
	private String table = "Subscription";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public SubscriptionORM insertGetId(SubscriptionORM subscription)
			throws Exception {
		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert IGNORE into "
								+ this.table
								+ " (accountId,createdAt,updatedAt,department_id) values (?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, subscription.getAccountId());
		stmt.setString(2, subscription.getCreatedAt());
		stmt.setString(3, subscription.getCreatedAt());
		stmt.setLong(4, subscription.getDepartment_id());

		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			subscription.setId(generatedKeys.getLong(1));
		} else {
			throw new SQLException(
					"Creating Subscription failed, no generated key obtained.");
		}
		// executeUpdate(query, Statement.RETURN_GENERATED_KEYS));
		return subscription;
	}

}
