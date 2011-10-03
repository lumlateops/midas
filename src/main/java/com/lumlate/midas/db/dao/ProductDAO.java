package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.ProductORM;

public class ProductDAO {
	private MySQLAccess access;
	private String table = "Product";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}
	public ProductORM getIDbyItem(ProductORM p) {
		try {
			stmt = this.access.getConn().prepareStatement(
					"Select id,category_id from " + this.table
							+ " where item=?",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, p.getItem());
			generatedKeys = stmt.executeQuery();
			if (generatedKeys.next()) {
				p.setId(generatedKeys.getLong(1));
				p.setCategory_id(generatedKeys.getLong(2));
				return p;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
