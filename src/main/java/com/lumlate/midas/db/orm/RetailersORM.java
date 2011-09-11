package com.lumlate.midas.db.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Vector;

import com.lumlate.midas.user.Retailers;

public class RetailersORM {
	private Vector<Retailers> retailers;
	private Statement stmt;
	private String table="Retailers";

	/***
	public void Insert(Retailer retailer) throws Exception{
		String str="INSERT INTO "+table+" (event_hour, story_id,app_endpoint,story_position,context,story_index,view_count,click_count) VALUES("+
        '"'+ctr_table_row.getEvent_hour()+'"'+","+'"'+ctr_table_row.getStory_id()+'"'
        +","+'"'+ctr_table_row.getApp_endpoint()+'"'+","+'"'+ctr_table_row.getStory_position()+'"'+","+'"'+ctr_table_row.getContext()+'"'+","+'"'+ctr_table_row.getStory_index()+'"'
        +","+'"'+ctr_table_row.getView_count()+'"'+","+'"'+ctr_table_row.getClick_count()+'"'+") ON DUPLICATE KEY UPDATE view_count=view_count+"+
        ctr_table_row.getView_count()+",click_count=click_count+"+ctr_table_row.getClick_count();
		try{
			stmt.executeUpdate(str);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void InsertMany(Vector<CTR_Table> ctr_table_rows) throws Exception{
		for(CTR_Table ctr:ctr_table_rows){
			Insert(ctr);
		}
	}
	***/

	public Retailers getRetailer(String domain){
		String str="Select * from "+table+" where domain="+domain;
		try{
			ResultSet s=stmt.executeQuery(str);
		
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public LinkedList<String> getallRetailerDomains(){
		String str="Select domain from "+table;
		LinkedList<String> retaildomains=new LinkedList<String>();
		try{
			ResultSet s=stmt.executeQuery(str);
			while(s.next()){
				retaildomains.add(s.getString("domain"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return retaildomains;
	}
	
	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
