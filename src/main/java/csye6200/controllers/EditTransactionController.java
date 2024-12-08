package main.java.csye6200.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.TransactionDAO;
import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.TransactionType;
import main.java.csye6200.utils.AlertUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class EditTransactionController {
    @FXML private TextField descriptionField;
    @FXML private ComboBox<TransactionType> typeComboBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private TransactionDAO transactionDAO;
    private CategoryDAOImpl categoryDAO;
    private Transaction transaction;
    private Map<String, String> categoryMap;

    public void initialize() throws ClassNotFoundException, SQLException {
        try {
            transactionDAO = new TransactionDAO();
            categoryDAO = new CategoryDAOImpl();

            typeComboBox.getItems().setAll(TransactionType.values());
            typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateCategoryComboBox(newValue));

            // Add input validation
            descriptionField.textProperty().addListener((observable, oldValue, newValue) -> validateDescription());
            amountField.textProperty().addListener((observable, oldValue, newValue) -> validateAmount());
            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateDate());

            saveButton.setOnAction(e -> saveTransaction());
            cancelButton.setOnAction(e -> cancelEdit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        descriptionField.setText(transaction.getDescription());
        typeComboBox.getSelectionModel().select(transaction.getType());
        amountField.setText(String.valueOf(transaction.getAmount()));
        datePicker.setValue(transaction.getDate());
        updateCategoryComboBox(transaction.getType());
        categoryComboBox.getSelectionModel().select(categoryMap.get(transaction.getCategory()));
    }

    private void updateCategoryComboBox(TransactionType type) {
        if (type != null) {
            try {
                categoryMap = categoryDAO.getCategories(type.name());
                categoryComboBox.getItems().setAll(categoryMap.keySet());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveTransaction() {
    	
    	Stage currentStage = (Stage) descriptionField.getScene().getWindow();
        if (!validateInputs()) {
            return;
        }

        try {
            String description = descriptionField.getText();
            TransactionType type = typeComboBox.getValue();
            String category = categoryComboBox.getValue();
            double amount = Double.parseDouble(amountField.getText());
            LocalDate date = datePicker.getValue();

            transaction.setDescription(description);
            transaction.setType(type);
            transaction.setCategory(categoryMap.get(category));
            transaction.setAmount(amount);
            transaction.setTransactionDate(date);
            
            boolean success = transactionDAO.updateTransaction(transaction);
            if (success) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Transaction saved successfully!", currentStage);
				closeWindow();
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Failed to save transaction.", currentStage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showAlert(Alert.AlertType.ERROR, "An error occurred while updating the transaction.", currentStage);
        }
    }

    private boolean validateInputs() {
        return validateDescription() && validateAmount() && validateDate() && validateType() && validateCategory();
    }

    private boolean validateDescription() {
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Description cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validateAmount() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, "Amount must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Invalid amount. Please enter a valid number.");
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        if (datePicker.getValue() == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please select a date.");
            return false;
        }
        return true;
    }

    private boolean validateType() {
        if (typeComboBox.getValue() == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please select a transaction type.");
            return false;
        }
        return true;
    }

    private boolean validateCategory() {
        if (categoryComboBox.getValue() == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please select a category.");
            return false;
        }
        return true;
    }

    private void cancelEdit() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}