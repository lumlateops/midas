package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

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
		stmt = this.access.getConn().prepareStatement(
				"select id,active,userInfo_id,email,provider_id,registeredEmail,dllrAccessToken,dllrTokenSecret from "
						+ this.table
						+ " where email=? and active=? and registeredEmail=?");
		stmt.setString(1, account.getEmail());
		stmt.setBoolean(2, true);
		stmt.setBoolean(3, true);
		generatedKeys = stmt.executeQuery();

		try {
			if (generatedKeys.next()) {
				account.setId(generatedKeys.getLong(1));
				account.setActive(generatedKeys.getBoolean(2));
				account.setUserid(generatedKeys.getLong(3));
				account.setEmail(generatedKeys.getString(4));
				account.setProvider_id(generatedKeys.getLong(5));
				account.setRegisteredEmail(generatedKeys.getBoolean(6));
				account.setDllrAccessToken(generatedKeys.getString(7));
				account.setDllrTokenSecret(generatedKeys.getString(8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;

	}

	public LinkedList<AccountORM> getAllAccounts() throws Exception {
		stmt = this.access.getConn().prepareStatement(
				"select id,active,userInfo_id,email,provider_id,registeredEmail,dllrAccessToken,dllrTokenSecret from "
						+ this.table
						+ " where active=? and registeredEmail=?");
		stmt.setBoolean(1, true);
		stmt.setBoolean(2, true);
		generatedKeys = stmt.executeQuery();
		LinkedList<AccountORM> accountlist = new LinkedList<AccountORM>();
		try {
			while (generatedKeys.next()) {
				AccountORM account = new AccountORM();
				account.setId(generatedKeys.getLong(1));
				account.setActive(generatedKeys.getBoolean(2));
				account.setUserid(generatedKeys.getLong(3));
				account.setEmail(generatedKeys.getString(4));
				account.setProvider_id(generatedKeys.getLong(5));
				account.setRegisteredEmail(generatedKeys.getBoolean(6));
				account.setDllrAccessToken(generatedKeys.getString(7));
				account.setDllrTokenSecret(generatedKeys.getString(8));
				accountlist.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountlist;
	}

	public LinkedList<AccountORM> getBatchAccounts(long minid, long maxid)
			throws Exception {
		stmt = this.access.getConn().prepareStatement(
				"select id,active,userInfo_id,email,provider_id,registeredEmail,dllrAccessToken,dllrTokenSecret from "
						+ this.table + " where id>=? and id<=? and active=? and registeredEmail=?");
		stmt.setLong(1, minid);
		stmt.setLong(2, maxid);
		stmt.setBoolean(3, true);
		stmt.setBoolean(4, true);
		
		generatedKeys = stmt.executeQuery();
		LinkedList<AccountORM> accontlist = new LinkedList<AccountORM>();
		try {
			while (generatedKeys.next()) {
				AccountORM account = new AccountORM();
				account.setId(generatedKeys.getLong(1));
				account.setActive(generatedKeys.getBoolean(2));
				account.setUserid(generatedKeys.getLong(3));
				account.setEmail(generatedKeys.getString(4));
				account.setProvider_id(generatedKeys.getLong(5));
				account.setRegisteredEmail(generatedKeys.getBoolean(6));
				account.setDllrAccessToken(generatedKeys.getString(7));
				account.setDllrTokenSecret(generatedKeys.getString(8));
				accontlist.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accontlist;
	}
}
