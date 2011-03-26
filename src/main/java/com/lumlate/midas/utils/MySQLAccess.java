package com.lumlate.midas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.Driver;

public class MySQLAccess {
	private String host;
	private String port;
	private String user;
	private String password;
	private String database;
	private String driver;
	private Connection conn;
	private String url;
	private Statement stmt;
	
	public MySQLAccess(String host, String port, String user, String password, String database){
		this.host=host;
		this.port=port;
		this.user=user;
		this.password=password;
		this.database=database;
		this.driver="com.mysql.jdbc.Driver";
		this.url = "jdbc:mysql://"+host+":"+port+"/";
		try {
			this.conn=Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.stmt=Statement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection Connect() throws Exception{
	    Class.forName(driver).newInstance();
	    return DriverManager.getConnection(this.url+this.database,this.user,this.password);
	}
	
	public void Dissconnect() throws SQLException{
		this.stmt.close();
		this.conn.close();
	}
	public Statement getStmt() {
		return stmt;
	}
	private Statement Statement() throws SQLException {
		return this.conn.createStatement();
	}
}