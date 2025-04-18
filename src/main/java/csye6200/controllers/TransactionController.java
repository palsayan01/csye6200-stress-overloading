package main.java.csye6200.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.TransactionDAO;
import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.TransactionType;
import main.java.csye6200.utils.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    @FXML private TextField descriptionField;
    @FXML private ComboBox<TransactionType> typeComboBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private Button backButton;

    private TransactionDAO transactionDAO;
    private Map<String, String> categoryMap;
    private CategoryDAOImpl categoryDAO;
    private String uid = SessionManager.getInstance().getUserId();

    @FXML
    public void initialize() {
        try {
            transactionDAO = new TransactionDAO();
            categoryDAO = new CategoryDAOImpl();

            typeComboBox.getItems().setAll(TransactionType.values());
            typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateCategoryComboBox(newValue));

            // Set up input validation
            setupInputValidation();

            saveButton.setOnAction(e -> saveTransaction());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupInputValidation() {
        // Validate amount field to allow only numbers and decimal point
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                amountField.setText(oldValue);
            }
        });

        // Validate description field to limit length
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) {
                descriptionField.setText(oldValue);
            }
        });

        // Set default date to today
        datePicker.setValue(LocalDate.now());
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
    	Stage currentStage = (Stage) saveButton.getScene().getWindow();
        if (!validateInputs(currentStage)) {
            return;
        }

        String description = descriptionField.getText();
        TransactionType type = typeComboBox.getValue();
        String category = categoryComboBox.getValue();
        double amount = Double.parseDouble(amountField.getText());
        LocalDate date = datePicker.getValue();
        String categoryId = categoryMap.get(category);

        Transaction transaction = new Transaction(description, amount, date, categoryId, type, uid);

        try {
            boolean success = transactionDAO.addTransaction(transaction);
            if (success) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Transaction saved successfully!", currentStage);
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Failed to save transaction.", currentStage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtils.showAlert(Alert.AlertType.ERROR, "An error occurred while saving the transaction.", currentStage);
        }
    }

    private boolean validateInputs(Stage currentStage) {
        if (descriptionField.getText().trim().isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please enter a description.", currentStage);
            return false;
        }

        if (typeComboBox.getValue() == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please select a transaction type.", currentStage);
            return false;
        }

        if (categoryComboBox.getValue() == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please select a category.", currentStage);
            return false;
        }

        if (amountField.getText().trim().isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please enter an amount.", currentStage);
            return false;
        }

        if (datePicker.getValue() == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Please select a date.", currentStage);
            return false;
        }

        return true;
    }

    private void clearInputs() {
        descriptionField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        categoryComboBox.getSelectionModel().clearSelection();
        amountField.clear();
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/transactionHistory.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initialize();
    }
}