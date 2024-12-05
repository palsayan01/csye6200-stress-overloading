package main.java.csye6200.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnect {
    private static DatabaseConnect instance; // Singleton instance
    private Properties dbProperties;
    private Connection connection;

    // Private constructor to restrict instantiation
    private DatabaseConnect() throws ClassNotFoundException, SQLException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("main/resources/properties/db.properties")) {
            dbProperties = new Properties();
            dbProperties.load(input);

            // Initialize the connection
            connection = initializeConnection();

            if (connection != null) {
                System.out.println("Database connected successfully.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new SQLException("Failed to load database properties", ex);
        }
    }

    // Get the singleton instance
    public static synchronized DatabaseConnect getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new DatabaseConnect();
        }
        return instance;
    }

    // Initialize the connection
    private Connection initializeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(dbProperties.getProperty("db.Driver"));

        String dbURL = dbProperties.getProperty("db.URL");
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbProperties.getProperty("db.User"));
        connectionProps.put("password", dbProperties.getProperty("db.Pwd"));
        connectionProps.put("connectTimeout", "5000");  // Connection timeout
        connectionProps.put("socketTimeout", "10000");  // Socket timeout

        return DriverManager.getConnection(dbURL, connectionProps);
    }

    // Provide the connection to DAOs
    public Connection getConnection() {
        return connection;
    }

    // Close the connection (optional)
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
