package main.java.csye6200.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TrackBudgetGoal implements Initializable {
	@FXML
	private PieChart pieChartBudget;
	
	@FXML
	private ProgressBar goalProgress;
	
	@FXML
	private HBox hboxBudget;
	private double progress;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		hboxBudget.setVisible(false);
		
	}
	
	@FXML
	private void trackBudget() {
		// TODO Auto-generated method stub
		hboxBudget.setVisible(true);
		ObservableList<PieChart.Data> budget = FXCollections.observableArrayList(
                new PieChart.Data("Grocery", 20),
                new PieChart.Data("Housing", 60),
                new PieChart.Data("Shopping", 20)
        );
		pieChartBudget.getData().addAll(budget);
		
		goalProgress.setStyle("-fx-accent: green;");
		goalProgress.setProgress(0.50);
	
		
		
	}

}
