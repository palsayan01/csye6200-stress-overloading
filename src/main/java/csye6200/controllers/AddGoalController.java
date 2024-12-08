package main.java.csye6200.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.GoalDAOImpl;
import main.java.csye6200.models.Goal;
import main.java.csye6200.utils.*;

public class AddGoalController implements Initializable {
	
	@FXML
	private TextField goalNameId;

	@FXML
	private TextField amountId;
	private Double amount;

	@FXML
	private DatePicker dateId;
	private LocalDate targetDate;


	@FXML
	private Button backButtonId;

	@FXML
	private Button goalSubmitId;
	
	private GoalDAOImpl goalDAO;
	private int result;
	private String userID;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		SessionManager session =SessionManager.getInstance();
		if (session !=null) {
			userID = session.getUserId();
		}
		try {
			goalDAO = new GoalDAOImpl();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void addGoal() {
		Stage currentStage = (Stage) goalNameId.getScene().getWindow();
		try {
			// Validate input
			if (goalNameId.getText().isEmpty() || amountId.getText() == null || dateId.getValue() == null) {
				AlertUtils.showAlert(Alert.AlertType.WARNING, "Please fill all the details", currentStage);
				return;
			}
			
			else if (goalNameId.getText().length() > 100) { // Example length validation
	            new Alert(Alert.AlertType.WARNING, "Goal description should not exceed 100 characters").showAndWait();
	            return;
	        }

			// Parse amount
			try {
				amount = Double.parseDouble(amountId.getText());
				if (amount <= 0) {
					AlertUtils.showAlert(Alert.AlertType.ERROR, "Amount must be a positive number", currentStage);
	                return;
	            }
				targetDate = dateId.getValue();
				if (targetDate.isBefore(LocalDate.now())) {
		            new Alert(Alert.AlertType.WARNING, "Target date cannot be in the past").showAndWait();
		            return;
		        }
			} catch (NumberFormatException e) {
				AlertUtils.showAlert(Alert.AlertType.ERROR, "Amount must be a valid number", currentStage);
                return;
            } catch (Exception ex) {
            	new Alert(Alert.AlertType.ERROR, "Invalid date format. Please enter a valid date").showAndWait();
                return;
            }
			
			Goal goal = new Goal();
			goal.setGoalId();
			goal.setGoalName(goalNameId.getText());
			goal.setTargetAmount(amount);
			goal.setDueDate(targetDate);
			result = goalDAO.createGoal(goal, userID);

			if (result == 1) {
				AlertUtils.showAlert(Alert.AlertType.CONFIRMATION, "New goal created successfully", currentStage);
			}
			else {
				AlertUtils.showAlert(Alert.AlertType.CONFIRMATION, "Failed to create a new goal", currentStage);
            }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void goBack() {
		Stage stage = (Stage) backButtonId.getScene().getWindow();
		stage.close();

	}

}
