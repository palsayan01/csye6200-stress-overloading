package main.java.csye6200.models;

import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;
public class User {

	// Unique identifier for a user
	private String userID;

    private String firstName;
    
    private String lastName;
    
    private String email;
    
    private String password;
    
    /**
     * Default constructor initializes the userId
     */
    public User(){
    	
    	this.userID = UUID.randomUUID().toString();
    }
    
    /**
     * Parameterized constructor to initialize a user with all attributes.
     * @param userID user's ID
     * @param name user's name
     * @param email user's email
     * @param password user's password

     */
    
    // Constructor to create a new user
    public User(String userID, String firstName,String lastName, String email, String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;  
    }
    
 // Getters and setters
    
	/**
     * Get user's id.
     * @return user's id
     */
    public String getUserID() {
        return userID;
    }
	/**
     * Set user's id.
     * @param user's id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

	/**
     * Get user's First Name.
     * @return user'sName
     */
    public String getFirstname() {
        return firstName;
    }
	/**
     * Set user's Last Name.
     * @param user's Name
     */
    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

	/**
     * Get user's Last Name.
     * @return user'sName
     */
    public String getLastname() {
        return lastName;
    }
	/**
     * Set user's Last Name.
     * @param user's Name
     */
    public void setLastname(String lastName) {
        this.lastName = lastName;
    }
	/**
     * Get user's Email
     * @return user's Email
     */
    public String getEmail() {
        return email;
    }
	/**
     * Set user's Email
     * @param user's Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

	/**
     * Get user's password.
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

	/**
     * Set user's password.
     * @param user's password
     */
    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

	/**
     * Override of the toString method to provide a string representation of the User object.
     * @return String representation of the User object
     */
	@Override
	public String toString() {
		return "User [id=" + userID + ", "
				   + "email=" + email + ", "
				   + "password=" + password + ","
				   + " firstName=" + firstName + ","
				   + "lastName=  " + lastName + "]";
	}
    
 // Method to validate user credentials (for login)
    public boolean validateCredentials(String email, String password) {
        // check against  database (to be implemented)
        return this.email.equals(email) && this.password.equals(password);
    }
}
