package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.DepartmentORM;

public class DepartmentDAO {

	private MySQLAccess access;
	private String table = "Department";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;
	private Gson g;
	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
		g = new Gson();
	}

	public DepartmentORM insertGetId(DepartmentORM department) throws Exception {

		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert into "
								+ this.table
								+ " (createdAt,email,logo,name,updatedAt,retailer_id) values (?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, department.getCreatedAt());
		stmt.setString(2, department.getEmail());
		stmt.setString(3, department.getLogo());
		stmt.setString(4, department.getName());
		stmt.setString(5, department.getUpdatedAt());
		stmt.setLong(6, department.getRetailerId());
		try{
			this.stmt.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
		this.generatedKeys = stmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			department.setId(generatedKeys.getLong(1));
		} else {
			throw new SQLException(
					"Creating Department failed, no generated key obtained.");
		}
		// executeUpdate(query, Statement.RETURN_GENERATED_KEYS));
		return department;
	}

}
