package main.java.csye6200.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnect {
	private static DatabaseConnect obj;
	private Properties dbProperties;
	private Connection con;

//	public static DatabaseConnect getInstance() throws ClassNotFoundException, SQLException {
//		if (obj == null) {
//			obj = new DatabaseConnect();
//		}
//		return obj;
//	}
	
	public DatabaseConnect() throws ClassNotFoundException, SQLException {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("main/resources/properties/db.properties")) {
			dbProperties = new Properties();
			dbProperties.load(input);
			this.con = getConnection();
			if(this.con!=null) {
				System.out.println("Connected!!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(dbProperties.getProperty("db.Driver"));
		String dbURL = dbProperties.getProperty("db.URL");
		String user = dbProperties.getProperty("db.User");
		String pwd = dbProperties.getProperty("db.Pwd");
		con = DriverManager.getConnection(dbURL, user, pwd);
		return con;
	}

}
