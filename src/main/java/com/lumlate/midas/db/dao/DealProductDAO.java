package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.DealProductORM;

public class DealProductDAO {
	private MySQLAccess access;
	private String table = "Deal_Product";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public DealProductORM insertGetId(DealProductORM dealproductrow) throws Exception {

		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert into "
								+ this.table
								+ " (Deal_id,products_id) values (?,?)",
						Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, dealproductrow.getDealId());
		stmt.setLong(2, dealproductrow.getProductId());

		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		try{
			if (generatedKeys.next()) {
				dealproductrow.setId(generatedKeys.getLong(1));
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
		return dealproductrow;
	}
	
	public DealProductORM[] multiInsertGetIds(DealProductORM[] dealorms) throws Exception {
		stmt= this.access.getConn().prepareStatement("Insert into " +this.table + " (Deal_id,products_id) values (?,?)",
				Statement.RETURN_GENERATED_KEYS);
		for(DealProductORM dealorm:dealorms){
			stmt.setLong(1, dealorm.getDealId());
			stmt.setLong(2, dealorm.getProductId());
			stmt.addBatch();
		}
		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		try{
			int flag=0;
			while (generatedKeys.next()) {
				dealorms[flag].setId(generatedKeys.getLong(1));
				flag+=1;
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
		return dealorms;
	}
	
}
