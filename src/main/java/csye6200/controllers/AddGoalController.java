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
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.GoalDAOImpl;
import main.java.csye6200.models.Goal;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			goalDAO = new GoalDAOImpl();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void addGoal() {
		try {
			// Validate input
			if (goalNameId.getText().isEmpty() || amountId.getText() == null || dateId.getValue() == null) {
				new Alert(Alert.AlertType.WARNING, "Please fill all the details").showAndWait();
				return;
			}

			// Parse amount
			try {
				amount = Double.parseDouble(amountId.getText());
				if (amount <= 0) {
					new Alert(Alert.AlertType.ERROR, "Amount must be a positive number").showAndWait();
	                return;
	            }
			} catch (NumberFormatException e) {
				new Alert(Alert.AlertType.ERROR, "Amount must be a valid number").showAndWait();
                return;
            }
			
			targetDate = dateId.getValue();
			Goal goal = new Goal();
			goal.setGoalId();
			goal.setGoalName(goalNameId.getText());
			goal.setTargetAmount(amount);
			goal.setDueDate(targetDate);
			result = goalDAO.createGoal(goal);

			if (result == 1) {
				new Alert(Alert.AlertType.CONFIRMATION, "New goal created successfully").showAndWait();
			}
			else {
				new Alert(Alert.AlertType.CONFIRMATION, "Failed to create a new goal").showAndWait();
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
