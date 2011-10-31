package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.ServiceProviderORM;

public class ServiceProviderDAO {
	private long id;
	private MySQLAccess access;
	private String table = "ServiceProvider";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public ServiceProviderORM getProviderbyId(ServiceProviderORM provider) throws Exception {
		stmt = this.access.getConn().prepareStatement(
				"select id,active,consumerKey,consumerSecret,createdAt,logoUrl,name,protocol,updatedAt,website from "
						+ this.table
						+ " where id=?");
		stmt.setLong(1, provider.getId());
		generatedKeys = stmt.executeQuery();
		try {
			if (generatedKeys.next()) {
				provider.setId(generatedKeys.getLong(1));
				provider.setActive(generatedKeys.getBoolean(2));
				provider.setConsumerKey(generatedKeys.getString(3));
				provider.setConsumerSecret(generatedKeys.getString(4));
				provider.setCreatedAt(generatedKeys.getString(5));
				provider.setLogoUrl(generatedKeys.getString(6));
				provider.setName(generatedKeys.getString(7));
				provider.setProtocol(generatedKeys.getString(8));
				provider.setUpdatedAt(generatedKeys.getString(9));
				provider.setWebsite(generatedKeys.getString(10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return provider;
	}
}
