package main.java.csye6200.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnect {
	private static DatabaseConnect obj;
	private Properties dbProperties;

	public static DatabaseConnect getInstance() {
		if (obj == null) {
			obj = new DatabaseConnect();
		}
		return obj;
	}
	
	public DatabaseConnect() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
			dbProperties = new Properties();
			dbProperties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
