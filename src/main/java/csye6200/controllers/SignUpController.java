package main.java.csye6200.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

/**
 * Sign up controller for user account creation logic
 * */
public class SignUpController implements Initializable {
	
	    @FXML
	    private TextField firstName;

	    @FXML
	    private TextField lastName;

	    @FXML
	    private TextField emailField;

	    @FXML
	    private PasswordField pwField;

	    @FXML
	    private PasswordField cpwField;
	    
	    @FXML
	    private Button btnSignup;
	    
	    @FXML
	    private Label linkLogin;
	    
	    private UserDAOImpl userDAO;
	    
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub	
			try {
		        this.userDAO = new UserDAOImpl();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        Platform.runLater(() -> {
	            // Remove focus from the first TextField (or set focus to another element)
	            firstName.getParent().requestFocus();
	        });
			
	        // Set the Login link action
	        linkLogin.setOnMouseClicked(event -> navigateToLoginPage());
		}
		
		
		@FXML
		public void createUserAccount(ActionEvent event) throws IOException, ClassNotFoundException {
			
			
		    // Get the input values from the UI fields
		    String firstNametxt = firstName.getText();
		    String lastNametxt = lastName.getText();
		    String email = emailField.getText();
		    String password = pwField.getText();
		    String confirmPassword = cpwField.getText();
		    
		    // Check if any field is empty
		    if (firstNametxt.isEmpty() || lastNametxt.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
		        showAlert(Alert.AlertType.ERROR,"All fields must be filled.");
		        return;
		    }
		    
		    
		    //Check for only alphabets in name
        	if (!firstNametxt.matches("[a-zA-Z]+")) {
        		showAlert(Alert.AlertType.ERROR,"First name should contain only alphabets");
        		return;
        	} 
        	
        	if (!lastNametxt.matches("[a-zA-Z]+")) {
        		showAlert(Alert.AlertType.ERROR,"Last name should contain only alphabets");
        		return;
        	} 
        	
            // Check for valid email format
            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR,"Please enter a valid email address.");
                return;  
            }
		    	    
		    // Check for password length and strength (at least 8 characters and includes digits, upper/lowercase, special characters)
		    if (password.length() < 8) {
		        showAlert(Alert.AlertType.ERROR,"Password should be at least 8 characters long.");
		        return;
		    }
		    
		    if (!isPasswordValid(password)) {
		        showAlert(Alert.AlertType.ERROR,"Password should have at least one lowercase, one uppercase letter, one digit, and one special character.");
		        return;
		    }
		    
		    // Validate password match
		    if (!password.equals(confirmPassword)) {
		        showAlert(Alert.AlertType.ERROR,"Passwords do not match.");
		        return;
		    }
		    
		    // Check if user already exists
		    int exists = userDAO.userExists(email);  // Use the userExists method to check if email is taken
		    if (exists == 1) {
		        showAlert(Alert.AlertType.ERROR,"An account with this email already exists. Please use a different email.");
		        return;
		    }
		    
		    //Creating new user
		    User newUser = new User();  // Create a new User object
		    newUser.setFirstname(firstNametxt);
		    newUser.setLastname(lastNametxt);
		    newUser.setEmail(email);
		    newUser.setPassword(password);
		    
		    //Saving the new user to database
		    int status = userDAO.saveUser(newUser);  // Calling the save method from UserDAOImpl

		    // If the user was successfully saved to the database, show success, otherwise show failure
		    if (status == 1) {
		        showAlert(Alert.AlertType.INFORMATION,"Registration successful!");
		        navigateToLoginPage();  // Navigate back to the Login screen
		    } else {
		        showAlert(Alert.AlertType.ERROR,"User could not be created. Please try again.");
		    }
		    
		    
		}
		

		//Helper function to check for strong password
		public static boolean isPasswordValid(String password) {
		    boolean upperCase = false;
		    boolean lowerCase = false;
		    boolean hasDigit = false;
		    boolean hasSpecialCharacter = false;
		    
		    String specialChars = "^&$@=#+%!";
		    
		    for (char ch : password.toCharArray()) {
		        if (Character.isUpperCase(ch)) upperCase = true;
		        else if (Character.isLowerCase(ch)) lowerCase = true;
		        else if (Character.isDigit(ch)) hasDigit = true;
		        else if (specialChars.indexOf(ch) != -1) hasSpecialCharacter = true;
		    }
		    
		    return upperCase && lowerCase && hasDigit && hasSpecialCharacter;
		}
		
		//Helper function to validate email format
		public static boolean isValidEmail(String email) {
		    // Regular expression for a valid email format
		    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		    return email.matches(emailRegex);  // Returns true if email matches the regex
		}

		public void navigateToLoginPage()  {
			try {
		        // Close the current login window
		        Stage stage = (Stage) btnSignup.getScene().getWindow();
		        stage.close();
		        // Open the login screen 
		        Stage loginStage = new Stage();
		        Parent loginRoot;
				loginRoot = FXMLLoader.load(getClass().getResource("/main/resources/fxml/LoginPage.fxml"));
		        Scene dashboardScene = new Scene(loginRoot);
		        loginStage.setScene(dashboardScene);
		        loginStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            showAlert(Alert.AlertType.ERROR, "Error loading the login page.");

			}



		}
		//Helper function to display alert messages
	    private void showAlert(Alert.AlertType alertType, String message) {
	        Alert alert = new Alert(alertType);
	        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

		
}

