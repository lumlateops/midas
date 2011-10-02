package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.DealCategoryORM;

public class DealCategoryDAO {
	
	private MySQLAccess access;
	private String table = "DealCategory";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}
	
	public DealCategoryORM getDealCategory(DealCategoryORM dealcategoryorm) {
		try {
			stmt = this.access.getConn().prepareStatement(
					"Select id,category,description from " + this.table
							+ " where category=?",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, dealcategoryorm.getCategory());
			generatedKeys = stmt.executeQuery();
			if (generatedKeys.next()) {
				dealcategoryorm.setId(generatedKeys.getLong(1));
				dealcategoryorm.setCategory(generatedKeys.getString(2));
				dealcategoryorm.setDescription(generatedKeys.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dealcategoryorm;
	}
}
