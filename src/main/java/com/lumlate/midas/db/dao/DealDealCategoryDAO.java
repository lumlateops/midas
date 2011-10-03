package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.DealDealCategoryORM;
import com.lumlate.midas.db.orm.DealProductORM;

public class DealDealCategoryDAO {
	private MySQLAccess access;
	private String table = "Deal_DealCategory";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public DealDealCategoryORM insertGetId(DealDealCategoryORM dealdealcategoryrow) throws Exception {

		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert into "
								+ this.table
								+ " (Deal_id,category_id) values (?,?)",
						Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, dealdealcategoryrow.getDealId());
		stmt.setLong(2, dealdealcategoryrow.getCategoryId());

		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		try{
			if (generatedKeys.next()) {
				dealdealcategoryrow.setId(generatedKeys.getLong(1));
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
		return dealdealcategoryrow;
	}
	
	public List<DealDealCategoryORM> multiInsertGetIds(List<DealDealCategoryORM> dealdealcategoryorms) throws Exception {
		stmt= this.access.getConn().prepareStatement("Insert into " +this.table + " (Deal_id,category_id) values (?,?)",
				Statement.RETURN_GENERATED_KEYS);
		for(DealDealCategoryORM dealdealcategoryorm:dealdealcategoryorms){
			stmt.setLong(1, dealdealcategoryorm.getDealId());
			stmt.setLong(2, dealdealcategoryorm.getCategoryId());
			stmt.addBatch();
		}
		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		try{
			int flag=0;
			while (generatedKeys.next()) {
				dealdealcategoryorms.get(flag).setId(generatedKeys.getLong(1));
				flag+=1;
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
		return dealdealcategoryorms;
	}
	
}
