package main.java.csye6200.utils;

import main.java.csye6200.models.User;

public class SessionManager {

    private static SessionManager instance;
    private String currentUserId;
    private boolean isLoggedIn;

    // Private constructor to prevent instantiation
    private SessionManager() {}

    // Method to get the Singleton instance of SessionManager
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Method to set the logged-in user
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
        this.isLoggedIn = true;
    }

    // Method to get the currently logged-in user
    // Get the current user's ID
    public String getUserId() {
        return currentUserId;
    }

    // Method to check if a user is logged in
    public boolean isUserLoggedIn() {
    	return isLoggedIn;
    }

    // Method to logout (clear session)
    public void logout() {
    	this.currentUserId = null;  // Reset userID
        this.isLoggedIn = false;  // Mark user as logged out
    }
    
}

