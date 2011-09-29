package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.AccountORM;

public class AccountDAO {
	private long id;
	private MySQLAccess access;
	private String table = "Account";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public AccountORM getIDfromEmail(AccountORM account) throws Exception {
		stmt = this.access.getConn()
				.prepareStatement(
						"select id,active,userInfo_id from " + this.table
								+ " where email=?");
		stmt.setString(1, account.getEmail());

		generatedKeys=stmt.executeQuery();
		
		//this.generatedKeys = stmt.getGeneratedKeys();
		try{
			if (generatedKeys.next()) {
				account.setId(generatedKeys.getLong(1));
				account.setActive(generatedKeys.getBoolean(2));
				account.setUserid(generatedKeys.getLong(3));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return account;

	}
}
