package com.developer;

import java.sql.*;
import java.util.ArrayList;

public class AccessToJson {
	
	private static AccessToJson instancia = null;
	
	private Connection con;
	private Statement st;
	private ResultSet r;
	
	private boolean verbose = false;
	
	ArrayList<String> title;
	ArrayList<Integer> column;
	
	private AccessToJson() {
		title = new ArrayList<>();
		column = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static AccessToJson start() {
		if (instancia == null) {
			instancia = new AccessToJson();
		}
		return instancia;
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public void setPath(String PathAccessDB) {
		try {
			con = DriverManager.getConnection("jdbc:ucanaccess://" + PathAccessDB);
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );
			getMessage("Success: Connection with Database");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Something goes wrong!");
			getMessage("Failure: check Path of Database");
		}
	}
	
	public void setTable(String NameTableDB) {
		try {
			r = st.executeQuery("SELECT * FROM " + NameTableDB);
			getMessage("Success: sql command: SELECT * FROM " + NameTableDB);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Something goes wrong!");
			getMessage("Failure: check the Table name of Database");
		}
	}
	
	public void setColumn(String title, int column) {
		this.title.add(title);
		setColumnNumberACCESS(column);
	}
	
	private void setColumnNumberACCESS(int column) {
		this.column.add(column);
	}
	
	private void getMessage(String text) {
		if (verbose)
			System.out.println(text);
	}
	
	public void parseToJson(String fileToExport) {
	}
}
