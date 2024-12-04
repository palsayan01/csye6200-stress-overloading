package main.java.csye6200.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import main.java.csye6200.models.User;

public class UserDAOImpl {

    private DatabaseConnect dbConnection;
    private Connection con;
    private int result =0;
    
    public UserDAOImpl(DatabaseConnect dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    /**
     * Method to save a new user to the database.
     * @param user The User object to be saved.
     * @return 1 if the user was saved successfully, 0 otherwise.
     * @throws ClassNotFoundException 
     */
    
    public int saveUser(User user) throws ClassNotFoundException {
        
	       try {
	    	   // Establish connection to the database
	    	   con = dbConnection.getConnection();
	    	   
	    	   //SQL query to insert user record
	    	   String query = "INSERT INTO users (userID, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)";
	    	   
	    	   // Prepare the SQL statement
	    	   PreparedStatement preparedStatement = con.prepareStatement(query);
	           preparedStatement.setString(1, user.getUserID().toString()); // Save UUID as a string
	           preparedStatement.setString(2, user.getFirstname());
	           preparedStatement.setString(3, user.getLastname());
	           preparedStatement.setString(4, user.getEmail());
	           preparedStatement.setString(5, user.getPassword()); // Already hashed
	           
	           // Execute the query
	           result = preparedStatement.executeUpdate();
	           
	           
	       }catch (SQLException e) {
	           e.printStackTrace();
	       }
	       
	       return result > 0 ? 1 : 0;
     }
    
    /**
     * Method to check if a user already exists based on the email.
     * @param email The email to check if a user already exists.
     * @return 1 if the user already exists, 0 otherwise.
     * @throws ClassNotFoundException 
     */
    public int userExists(String email) throws ClassNotFoundException {
    	
    	try {
            // Establish connection to the database
            con = dbConnection.getConnection();
            
            // SQL query to check if the user with the given email exists
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";
            
            // Prepare the SQL statement
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Check if any result was returned
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                result = count > 0 ? 1 : 0;  // If count > 0, user exists, else no user found
              }
            } catch (SQLException e) {
                     e.printStackTrace();
            }
        return result;
     }  	
   
    /**
     * Method to authenticate a user based on credentials.
     * @param email The email to check .
     * @param password The password to check .
     * @return true if the user is authenticated, false otherwise.
     * @throws ClassNotFoundException 
     */
    public boolean authenticateUser(String email, String password) throws ClassNotFoundException {

        String query = "SELECT * FROM users WHERE email = ?";  

        try {
            con = dbConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                  return BCrypt.checkpw(password, storedPassword);
                    
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Return false if user doesn't exist or passwords don't match
    }
    
    /**
     * Method to retrieve  authenticated user info.
     * @param email The email.
     * @return user details 
     * @throws ClassNotFoundException 
     */
    
    public ResultSet authenticatedUserinfo(String email) throws ClassNotFoundException {
    	
        String query = "SELECT * FROM users WHERE email = ?";
        ResultSet resultSet = null;
        
        try {
        	  con = dbConnection.getConnection();
              PreparedStatement preparedStatement = con.prepareStatement(query);
              preparedStatement.setString(1, email);
              
              resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;  // Return the ResultSet with the user details
    }
    
    /**
     * Method to fetch  user details by id.
     * @param UserId .
     * @return user details 
     * @throws ClassNotFoundException 
     */
    public ResultSet getUserDetailsByUserId(String userId) throws ClassNotFoundException {
        String query = "SELECT firstName, lastName FROM users WHERE userID = ?";
        ResultSet resultSet = null;
        
        try {
        	con = dbConnection.getConnection();
        	PreparedStatement preparedStatement = con.prepareStatement(query);
        	preparedStatement.setString(1, userId);
        	
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}


    	
    	
    	
    	
    	
    	
    	
    	
        


