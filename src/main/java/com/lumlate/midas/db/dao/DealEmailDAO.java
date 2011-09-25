package com.lumlate.midas.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.lumlate.midas.db.MySQLAccess;
import com.lumlate.midas.db.orm.DealEmailORM;

public class DealEmailDAO {

	private long id;
	private MySQLAccess access;
	private String table = "DealEmail";
	private PreparedStatement stmt;
	private ResultSet generatedKeys = null;

	public void setAccess(MySQLAccess myaccess) {
		this.access = myaccess;
	}

	public DealEmailORM insertGetId(DealEmailORM dealemailrow) throws Exception {

		stmt = this.access
				.getConn()
				.prepareStatement(
						"Insert into "
								+ this.table
								+ " (categoryid,content,dateReceived,domainKey,fromEmail,fromName,parsedContent,senderIP,sentDate,spfResult,subject,toName) values (?,?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, dealemailrow.getCategory());
		stmt.setString(2, dealemailrow.getContent().substring(0, 254));
		stmt.setString(3, dealemailrow.getDateReceived());
		stmt.setString(4, dealemailrow.getDomainKey());
		stmt.setString(5, dealemailrow.getFromEmail());
		stmt.setString(6, dealemailrow.getFromName());
		stmt.setString(7, dealemailrow.getParsedContent().substring(0, 254));
		stmt.setString(8, dealemailrow.getSenderIP());
		stmt.setString(9, dealemailrow.getSentDate());
		stmt.setString(10, dealemailrow.getSpfResult());
		stmt.setString(11, dealemailrow.getSubject());
		stmt.setString(12, dealemailrow.getToName());

		this.stmt.executeUpdate();
		this.generatedKeys = stmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			dealemailrow.setId(generatedKeys.getLong(1));
		} else {
			throw new SQLException(
					"Creating user failed, no generated key obtained.");
		}
		// executeUpdate(query, Statement.RETURN_GENERATED_KEYS));
		return dealemailrow;
	}

}
