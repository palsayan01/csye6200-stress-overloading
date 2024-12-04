package main.java.csye6200.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.csye6200.dao.BudgetDAOImpl;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.GoalDAOImpl;

public class TrackBudgetGoal implements Initializable {
	@FXML
	private PieChart pieChartBudget;
	
	@FXML
	private ProgressBar goalProgress;
	
	@FXML
	private HBox hboxBudget;
	
	@FXML
	private Button addBudgetId;
	private double progress;
	private BudgetDAOImpl budgetDAO;
	private ResultSet rs;
	
	@FXML
	private ComboBox<String> monthId;
	private String month;

	@FXML
	private ComboBox<Integer> yearId;
	private int year;
	
	@FXML
	private Button addGoalId;
	
	@FXML
	private ComboBox<String> goalId;

	private GoalDAOImpl goalDAO;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		hboxBudget.setVisible(false);
		
	}
	
	@FXML
	private void trackBudget() throws ClassNotFoundException, SQLException {
		hboxBudget.setVisible(true);
		//Populate goal dropdown
		this.goalDAO = new GoalDAOImpl(new DatabaseConnect());
		rs = goalDAO.goals();
		List<String> goalList = new ArrayList<>();
		while(rs.next()) {
			goalList.add(rs.getString(1));
		}
		goalId.getItems().addAll(goalList);
		
		
		monthId.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December");

		IntStream.rangeClosed(2023, 2030).forEach(yearId.getItems()::add);

		monthId.setPromptText("Select Month");
		yearId.setPromptText("Select Year");
		
		monthId.valueProperty().addListener((observable, oldValue, newValue) -> {
	        try {
				updatePieChartIfReady();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    });

	    yearId.valueProperty().addListener((observable, oldValue, newValue) -> {
	        try {
				updatePieChartIfReady();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    });	
	    
	    goalId.setPromptText("Select Goal");
	    
	    
	    goalId.valueProperty().addListener((observable, oldValue, newValue) -> {
	    	checkGoalReady();
	    });	
	}
	
	private void checkGoalReady() {
		if (goalId.getValue() != null) {
		    try {
				showProgress();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
		    pieChartBudget.getData().clear();
		}
		
	}

	private void showProgress() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		String goalName = goalId.getValue();
		Double percent_achieved=0.0;
		int result = goalDAO.checkGoalAchieved(goalName);
		if (result>0) {
			rs = goalDAO.getProgress(goalName);
			while (rs.next()) {
				percent_achieved = rs.getDouble(1);
				
			}
			goalProgress.setStyle("-fx-accent: green;");
			goalProgress.setProgress(percent_achieved);
		}
		

		
	}

	private void updatePieChartIfReady() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		try {
			if (monthId.getValue() != null && yearId.getValue() != null) {
			    updatePieChart();
			} else {
			    pieChartBudget.getData().clear();
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updatePieChart() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		ResultSet rs2;
		this.budgetDAO = new BudgetDAOImpl(new DatabaseConnect());
		month = monthId.getValue();
		year=yearId.getValue();
		rs = budgetDAO.getBudgetDetails(month, year);
		if (!rs.isBeforeFirst()) {
			pieChartBudget.getData().clear();
			return;
		}
			
		ObservableList<PieChart.Data> budget = FXCollections.observableArrayList();
		while(rs.next()) {
			rs2 = budgetDAO.getTotalExpenseByCategory(rs.getString("category_id"), month, year);
			String categoryName = rs.getString("category_name");
	        double amount = rs.getDouble("amount");
	        double remainingBudget = 0;
			while(rs2.next()) {
				remainingBudget = amount - rs2.getDouble(1);
			}
			String label = String.format("%s (Budget: %.2f)", categoryName, amount);
			String labelRemaining = String.format("%s (Remaining: %.2f)", categoryName, remainingBudget);
			budget.add(new PieChart.Data(label, amount));
	        budget.add(new PieChart.Data(labelRemaining, remainingBudget));

			
		}
		pieChartBudget.getData().addAll(budget);
		
	}

	@FXML
	private void addBudget() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/manageBudget.fxml"));
        Parent popupContent = loader.load();

        // Create a new stage for the pop-up
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        popupStage.setScene(new Scene(popupContent));
        popupStage.showAndWait();
	}
	
	@FXML
	private void addGoal() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/setGoal.fxml"));
        Parent popupContent = loader.load();

        // Create a new stage for the pop-up
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        popupStage.setScene(new Scene(popupContent));
        popupStage.showAndWait();
	}

}
