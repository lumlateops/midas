package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.FetchHistoryORM;

public class FetchHistoryDAO {

	private MySQLAccess access;
	private String table = "FetchHistory";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public FetchHistoryORM insert(FetchHistoryORM fetchhistory)
			throws Exception {

		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert into "
								+ this.table
								+ " (fetchEndTime,fetchErrorMessage,fetchStartTime,fetchStatus,sessionid,userInfo_id) values (?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, fetchhistory.getFetchEndTime());
		stmt.setString(2, fetchhistory.getFetchErrorMessage());
		stmt.setString(3, fetchhistory.getFetchStartTime());
		stmt.setString(4, fetchhistory.getFetchStatus());
		stmt.setString(5, fetchhistory.getSessionid());
		stmt.setLong(6, fetchhistory.getUserid());
		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			fetchhistory.setId(generatedKeys.getLong(1));
		} else {
			throw new SQLException(
					"Creating user failed, no generated key obtained.");
		}
		// executeUpdate(query, Statement.RETURN_GENERATED_KEYS));
		return fetchhistory;
	}

	public String getLastFetchTime(long userId) {
		try {
			stmt = this.access
					.getConn()
					.prepareStatement(
							"Select fetchEndTime,fetchStartTime from "
									+ this.table
									+ " where userInfo_id=? and fetchStatus=? order by fetchEndTime desc limit 1",
							Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, userId);
			stmt.setString(2, "Success");
			generatedKeys = stmt.executeQuery();
			if (generatedKeys.next()) {
				return generatedKeys.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public FetchHistoryORM update(FetchHistoryORM fetchorm) throws Exception {
		stmt = this.access
				.getConn()
				.prepareStatement(
						"Update "
								+ this.table
								+ " set fetchEndTime=?,fetchErrorMessage=?,fetchStartTime=?,fetchStatus=?,sessionid=?,userInfo_id=? where id=?");
		stmt.setString(1, fetchorm.getFetchEndTime());
		stmt.setString(2, fetchorm.getFetchErrorMessage());
		stmt.setString(3, fetchorm.getFetchStartTime());
		stmt.setString(4, fetchorm.getFetchStatus());
		stmt.setString(5, fetchorm.getSessionid());
		stmt.setLong(6, fetchorm.getUserid());
		stmt.setLong(7, fetchorm.getId());
		try {
			this.stmt.executeUpdate();
		} catch (SQLException err) {
			throw new SQLException("updating fecthistory failed, no generated key obtained.");
		}
		return fetchorm;
	}
}
