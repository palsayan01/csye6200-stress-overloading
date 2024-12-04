package main.java.csye6200.controllers;

import java.io.IOException;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.UserDAOImpl;
import main.java.csye6200.models.User;
import main.java.csye6200.utils.SessionManager; 

public class LoginController implements Initializable  {
	
	    @FXML
	    private TextField emailField;  
	    @FXML
	    private PasswordField pwField;  
	    @FXML
	    private Button btnLogin; 
	    @FXML
	    private Label linkSignUp;
	    
	    private UserDAOImpl userDAO;
	    
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
			try {
		        this.userDAO = new UserDAOImpl(new DatabaseConnect());
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        Platform.runLater(() -> {
	            // Remove focus from the first TextField (or set focus to another element)
	        	emailField.getParent().requestFocus();
	        });
	        
	        // Set the Sign Up link action
//	        linkSignUp.setOnMouseClicked(event -> navigateToSignUpPage());
	    }
	    
	    
	    @FXML
	    public void loginUser(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
	        // Get the entered email and password
	        String email = emailField.getText();
	        String password = pwField.getText();

	        // Validate input (check if fields are empty)
	        if (email.isEmpty() || password.isEmpty()) {
	            showAlert(Alert.AlertType.WARNING, "Please enter both email and password.");
	            return;
	        }

	        // Authenticate the user using the UserDAOImpl
	        boolean isAuthenticated = userDAO.authenticateUser(email, password);
	        
	        if (isAuthenticated) {
	        	
	        	try {
	        		// If authenticated, store the user in session
		        	ResultSet resultSet = userDAO.authenticatedUserinfo(email);
	        		
		        	if (resultSet.next()) {
			        	String userId = resultSet.getString("userID");

			        	//Store the authenticated user's ID in the session
			            SessionManager.getInstance().setCurrentUser(userId);

			            showAlert(Alert.AlertType.INFORMATION, "Login successful!");
			            navigateToMainDashboard();
			            
			        	}
		        	else {
			            showAlert(Alert.AlertType.ERROR, "Invalid email or password.");
			            emailField.clear();
			            pwField.clear();
			        }
		        	resultSet.close();
	        	} catch (SQLException e) {
	                e.printStackTrace();
	     }
  	
	  } 
 }



	    // Method to navigate to the dashboard after successful login
	    private void navigateToMainDashboard() throws IOException {
	        // Close the current login window
	        Stage stage = (Stage) btnLogin.getScene().getWindow();
	        stage.close();

	        // Open the dashboard screen 
	        Stage dashboardStage = new Stage();
	        Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/main/resources/fxml/Dashboard_main.fxml"));
	        Scene dashboardScene = new Scene(dashboardRoot);
	        dashboardStage.setScene(dashboardScene);
	        dashboardStage.show();
	    }

//	    // Method to navigate to the sign-up page when the "Sign Up" link is clicked
//	    private void navigateToSignUpPage() {
//	        try {
//	            // Open the SignUp screen (assuming you have a SignUp.fxml)
//	            Stage signUpStage = new Stage();
//	            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/main/java/csye6200/views/SignUp.fxml"));
//	            Scene signUpScene = new Scene(signUpRoot);
//	            signUpStage.setScene(signUpScene);
//	            signUpStage.show();
//
//	            // Close the current login window
//	            Stage currentStage = (Stage) linkSignUp.getScene().getWindow();
//	            currentStage.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            showAlert(Alert.AlertType.ERROR, "Error loading the sign-up page.");
//	        }
//	    }
	    
	    
	    // Helper method to show alerts
	    private void showAlert(Alert.AlertType alertType, String message) {
	        Alert alert = new Alert(alertType);
	        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

}
