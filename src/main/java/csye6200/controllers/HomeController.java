package main.java.csye6200.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.UserDAOImpl;
import main.java.csye6200.utils.SessionManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeController {

	@FXML
    private Label welcomeLabel;  // Label to display the welcome message

	private UserDAOImpl userDAO;
	
	@FXML
    public void initialize() throws ClassNotFoundException  {
		try {
	        this.userDAO = new UserDAOImpl();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		

        // Retrieve userID from the session
        String userId = SessionManager.getInstance().getUserId();

        // Check if the user is logged in
        if (userId != null) {
            // Fetch user details based on userID
            try {
					ResultSet resultSet = userDAO.getUserDetailsByUserId(userId);
				
                if (resultSet != null && resultSet.next()) {
                    // Get first name and last name from the result set
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");

                    // Set the welcome message
                    welcomeLabel.setText("Welcome, " + firstName + " " + lastName + "!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // If user is not logged in, display an error or prompt
            welcomeLabel.setText("User not logged in");
        }
    }
	
}
