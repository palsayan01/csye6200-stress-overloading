package main.java.csye6200.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.csye6200.dao.BudgetDAOImpl;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.models.Budget;
import main.java.csye6200.models.Category;

public class AddBudgetController implements Initializable {

	@FXML
	private TextField amountId;
	private Double amount;

	@FXML
	private ComboBox<String> monthId;

	@FXML
	private ComboBox<Integer> yearId;

	@FXML
	private ComboBox<String> categoryId;
	private String category;

	@FXML
	private Button backButtonId;

	@FXML
	private Button budgetSubmitId;
	private BudgetDAOImpl budgetDAO;
	private CategoryDAOImpl categoryDAO;
	private int result;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			this.budgetDAO = new BudgetDAOImpl(new DatabaseConnect());
			this.categoryDAO = new CategoryDAOImpl(new DatabaseConnect());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		monthId.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December");

		IntStream.rangeClosed(2023, 2024).forEach(yearId.getItems()::add);

		categoryId.getItems().addAll("Groceries", "Transport", "Rent", "Utilities", "Entertainment");

		monthId.setPromptText("Select Month");
		yearId.setPromptText("Select Year");
		categoryId.setPromptText("Select Category");

	}

	@FXML
	public void addBudget() {
		try {
			// Validate input
			if (amountId.getText().isEmpty() || monthId.getValue() == null || yearId.getValue() == null
					|| categoryId.getValue() == null) {
				new Alert(Alert.AlertType.WARNING, "Please fill all fields").showAndWait();
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
			

			category = categoryId.getValue();
			Category cat = new Category();
			cat.setCategoryName(category);
			cat.setCategoryId();
			result = categoryDAO.createCategory(cat);

			Budget budget = new Budget();
			budget.setAmount(amount);
			budget.setBudgetId();
			budget.setRemainingAmount(amount);
			budget.setMonth(monthId.getValue());
			budget.setYear(yearId.getValue());
			budget.setCategory(cat);

			result = budgetDAO.createBudget(budget);
			if (result == 1) {
				new Alert(Alert.AlertType.CONFIRMATION, "New budget created successfully").showAndWait();
			}
			else {
				new Alert(Alert.AlertType.CONFIRMATION, "Failed to create a new budget").showAndWait();
            }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void goBack() {
		Stage stage = (Stage) backButtonId.getScene().getWindow();
		stage.close();

	}
}
