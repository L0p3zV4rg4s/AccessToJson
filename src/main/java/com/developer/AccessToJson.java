package com.developer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AccessToJson {
	
	private static AccessToJson instancia = null;
	
	private Connection con;
	private Statement st;
	private ResultSet r;
	
	BufferedWriter writeToJSON;

	private boolean verbose = false;
	
	ArrayList<String> title;
	ArrayList<Integer> column;
	
	private int[] groupColum;
	private String groupName = "";
	
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
		getMessage("Set verbose mode ON.");
		this.verbose = verbose;
	}
	
	public void setPath(String PathAccessDB, String NameTableDB) {
		/**
		 * Set the Path of the Database and the Table name to open it.
		 */
		try {
			if (verbose)
				getMessage("Opening the DB " + NameTableDB);
			con = DriverManager.getConnection("jdbc:ucanaccess://" + PathAccessDB);
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			if (verbose)
				getMessage("Success: Connection with Database.");
			r = st.executeQuery("SELECT * FROM " + NameTableDB);
			if (verbose)
				getMessage("Success: sql command: SELECT * FROM " + NameTableDB);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Something goes wrong!.");
			getMessage("Failure: check Path or Table name of Database.");
		}
	}
	
	public void setColumn(String title, int column) {
		/**
		 * Add to a ArrayList<> the different key and value.
		 */
		this.title.add(title);
		this.column.add(column);
	}
	
	public void setGroup(String title, int[] column) {
		/**
		 * If you need a Key and many value inside. Like a subgroup
		 * Key : Value,
		 *       Value,
		 *       Value (...)
		 */
		this.groupColum = column;
		this.groupName = title;
	}
	
	private void getMessage(String text) {
		if (verbose)
			System.out.println(text);
	}
	
	public void parseToJson(String fileToExport) {
		try {
			File file_exist = new File(fileToExport);
			if (file_exist.exists()) {
				System.out.println("File exist already, please choose a different file");
				return;
			}
			writeToJSON = new BufferedWriter(new FileWriter(fileToExport, true));
			if (verbose)
				getMessage("New file " + fileToExport + " created.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Something goes wrong!.");
			getMessage("Failure: Close the file " + fileToExport + ", and start again.");
		}
	}
	
	public void setToJSON() {
		JSONArray listJason = new JSONArray();
		JSONObject objJason = new JSONObject();
		
		String value;
		int count = 1;
		
		try {
			r.first();
			while (r.isLast() == false) {
				for (int x = 0; x < title.size(); x++) {
					value = r.getString(column.get(x));
					if (r.wasNull()) {
						value = "";
					}
					objJason.put(title.get(x),value);
				}
				// Only if a Group exist
				if (!(groupName.isEmpty())) {
					for (int x = 0; x < groupColum.length; x++) {
						value = r.getString(groupColum[x]);
						getMessage(groupName + " - " + value);
						listJason.add(value);
					}
					objJason.put(groupName, listJason);
				}
				writeToJSON.append(objJason.toString());
				writeToJSON.newLine();
				objJason.clear();
				listJason.clear();
				r.next();
				count++;
			}
			r.close();
			if (verbose)
				getMessage("Read " + count + " lines of the DB.");
			writeToJSON.close();
			if (verbose)
				getMessage("Success: Parse into JSON.");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}