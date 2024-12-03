package main.java.csye6200.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.csye6200.dao.TransactionHistoryDAO;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.Category;
import main.java.csye6200.models.TransactionType;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionHistoryController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, Category> categoryColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button backButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;
    
    @FXML
    private TableColumn<Transaction, String> fromToColumn;

    @FXML
    private Button searchButton;

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private TransactionHistoryDAO transactionHistoryDAO;

    public void initialize() {
        try {
            DatabaseConnect dbConnect = new DatabaseConnect();
            Connection connection = dbConnect.getConnection();
            transactionHistoryDAO = new TransactionHistoryDAO(connection);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadTransactionData();

        filterComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE", "All"));
    }

    private void loadTransactionData() {
        try {
            List<Transaction> transactions = transactionHistoryDAO.getAllTransactions();
            transactionList.clear();
            transactionList.addAll(transactions);
            transactionTable.setItems(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchTerm = searchTextField.getText();
        String filter = filterComboBox.getValue();

        try {
            List<Transaction> transactions = transactionHistoryDAO.searchTransactions(searchTerm, filter);
            transactionList.clear();
            transactionList.addAll(transactions);
            transactionTable.setItems(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            try {
                boolean deleted = transactionHistoryDAO.deleteTransaction(selectedTransaction.getId());
                if (deleted) {
                    transactionList.remove(selectedTransaction);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
	public void goBack() {
		Stage stage = (Stage) backButton.getScene().getWindow();
		stage.close();

	}
    @FXML
    private void handleBackAction(ActionEvent event) {
        // Implement navigation to the previous scene as needed.
        System.out.println("Back button pressed");
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        // Implement editing logic as needed.
        System.out.println("Edit button pressed");
    }
}
