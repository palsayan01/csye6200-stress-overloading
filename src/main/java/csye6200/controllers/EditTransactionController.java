package main.java.csye6200.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.TransactionDAO;
import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.TransactionType;

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
//    @FXML private Button backButton;

    private TransactionDAO transactionDAO;
    private CategoryDAOImpl categoryDAO;
    private Transaction transaction;
    private Map<String, String> categoryMap;

    public void initialize() throws ClassNotFoundException, SQLException {
    	try {
            transactionDAO = new TransactionDAO();
            categoryDAO = new CategoryDAOImpl(new DatabaseConnect());

            // Set default items for ComboBoxes
            typeComboBox.getItems().setAll(TransactionType.values());
            typeComboBox.valueProperty().addListener(new ChangeListener<TransactionType>() {
                @Override
                public void changed(ObservableValue<? extends TransactionType> observable, TransactionType oldValue, TransactionType newValue) {
                    updateCategoryComboBox(newValue); // Update categories based on the selected type
                }
            });
//            categoryMap = categoryDAO.getCategories();
//            categoryComboBox.getItems().setAll(categoryMap.keySet()); // Sample categories
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveButton.setOnAction(e -> saveTransaction());
        cancelButton.setOnAction(e -> cancelEdit());
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;

        // Pre-fill the fields with the transaction details
        descriptionField.setText(transaction.getDescription());
        typeComboBox.getSelectionModel().select(transaction.getType());
        amountField.setText(String.valueOf(transaction.getAmount()));
        datePicker.setValue(transaction.getDate());

        // Load categories for the selected type
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaction updated successfully!", ButtonType.OK);
                alert.showAndWait();

                closeWindow();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update transaction.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelEdit() {
//    	TransactionHistoryController.initialize();
        closeWindow();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
