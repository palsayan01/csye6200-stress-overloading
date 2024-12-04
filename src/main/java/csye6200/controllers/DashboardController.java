package main.java.csye6200.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML
    private StackPane contentArea;
    
    @FXML
    private Button btnBudgetandGoal;

    @FXML
    private Button btnAddTransaction;

    @FXML
    private Button btnHistory;
    
//    @FXML
//    private Button btnLogout;
    
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
    	btnAddTransaction.setOnAction(e -> loadScreen("transactionHistory"));
    	btnReport.setOnAction(e -> loadScreen("reports"));
    	btnNotifications.setOnAction(e -> loadScreen("notifications"));
//    	btnLogout.setOnAction(e -> handleLogout());
    }
    
    
    /**
     * Loads the specified view into the contentPane.
     * 
     * @param viewName The name of the view to load (e.g., "home", "profile").
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
}