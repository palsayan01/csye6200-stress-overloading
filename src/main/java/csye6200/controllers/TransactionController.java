package main.java.csye6200.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.dao.TransactionDAO;
import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.TransactionType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.util.Map;

public class TransactionController {
    @FXML private TextField descriptionField;
    @FXML private ComboBox<TransactionType> typeComboBox;
    @FXML private ComboBox<String> categoryComboBox; // Category is now a String
    @FXML private TextField amountField;
    @FXML private TextField fromToField;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;

    private TransactionDAO transactionDAO;
    private Map<String, String> categoryMap; 
    private CategoryDAOImpl categoryDAO;
    @FXML
    public void initialize() {
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
    }
    private void updateCategoryComboBox(TransactionType type) {
        if (type != null) { 

        try {
            // Retrieve categories dynamically based on the selected type
            categoryMap = categoryDAO.getCategories(type.name()); // Assuming type is passed as a string like "INCOME" or "EXPENSE"
            categoryComboBox.getItems().setAll(categoryMap.keySet()); // Populate ComboBox with category names
        } catch (Exception e) {
            e.printStackTrace();
        }}
    }
    private void saveTransaction() {
        String description = descriptionField.getText();
        TransactionType type = typeComboBox.getValue();
        String category = categoryComboBox.getValue();  // Category is a String
        double amount = Double.parseDouble(amountField.getText());
        LocalDate date = datePicker.getValue();
//        String categoryId = categoryMap.get(category); 
        
        
        String categoryId = categoryMap.get(category);
        
        System.out.println(description + amount +  date +  categoryId + type);
        Transaction transaction = new Transaction(description, amount, date, categoryId, type);

        try {
            boolean success = transactionDAO.addTransaction(transaction);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transaction saved successfully!", ButtonType.OK);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save transaction.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
