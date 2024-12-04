package main.java.csye6200.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.csye6200.utils.SessionManager;

public class DashboardController {

    @FXML
    private StackPane contentArea;
    
    @FXML
    private Button btnBudgetandGoal;

    @FXML
    private Button btnAddTransaction;

    @FXML
    private Button btnHistory;
    
    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnReport;

    @FXML
    private Button btnNotifications;
    
    /**
     * Loads Screen 1 into the content area.
     */
    @FXML
    private void initialize() {
    	
    	loadScreen("HomePage");
    	
        // Add button actions
    	btnBudgetandGoal.setOnAction(e -> loadScreen("budgetgoal-home"));
    	btnHistory.setOnAction(e -> loadScreen("transactionHistory"));
    	btnReport.setOnAction(e -> loadScreen("reports"));
    	btnNotifications.setOnAction(e -> loadScreen("notifications"));
    	btnLogout.setOnAction(e -> handleLogout());
    }
    
    
    /**
     * Loads the specified screen into the contentArea.
     * 
     * @param viewName The name of the screen to load.
     */
    private void loadScreen(String screenName) {
        try {
            // Clear the current content
        	if(this.contentArea!=null) {
        		 contentArea.getChildren().clear();
        	}
           

            // Dynamically load the view
            Node view = FXMLLoader.load(getClass().getResource("/main/resources/fxml/" + screenName + ".fxml"));
            contentArea.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Handles the logout action. Clears the session and navigate to the login page
     * 
     */
    
    private void handleLogout() {
        // Clear the session (logout)
        SessionManager.getInstance().logout();

        // Redirect the user to the login screen
        try {
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();  // Get the current window (Stage)
            currentStage.close();  // Close the dashboard window

            // Load the login screen
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/LoginPage.fxml"));
            Scene loginScene = new Scene(loader.load());
            loginStage.setScene(loginScene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
