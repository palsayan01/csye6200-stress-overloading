package main.java.csye6200.controllers;

import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.csye6200.dao.TransactionHistoryDAO;
import main.java.csye6200.dao.CategoryDAOImpl;
import main.java.csye6200.dao.DatabaseConnect;
import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.Category;
import main.java.csye6200.models.TransactionType;
import main.java.csye6200.utils.SessionManager;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TransactionHistoryController implements Initializable  {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

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
    private Button navigateToAddTransactionButton;
    
    @FXML
    private TableColumn<Transaction, String> fromToColumn;

    @FXML
    private Button searchButton;

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private TransactionHistoryDAO transactionHistoryDAO;
    private CategoryDAOImpl categoryDAO;
    private Map<String, String> categoryMap; 
    private String userid = SessionManager.getInstance().getUserId();

    public void initialize() {
        try {
            DatabaseConnect dbConnect = new DatabaseConnect();
            Connection connection = dbConnect.getConnection();
            transactionHistoryDAO = new TransactionHistoryDAO(connection);
            categoryDAO = new CategoryDAOImpl(new DatabaseConnect());
            categoryMap = categoryDAO.getAllCategories();
            System.out.println(categoryMap);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(cellData -> {
            String categoryKey = cellData.getValue().getCategory(); 
            String categoryValue = categoryMap.get(categoryKey);
            System.out.println(categoryKey);
            System.out.println("categoryId" + categoryMap.get(categoryKey));
            return new SimpleStringProperty(categoryValue);
        });
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        loadTransactionData();

        filterComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE", "All"));
    }

    private void loadTransactionData() {
        try {
            List<Transaction> transactions = transactionHistoryDAO.getAllTransactions(userid);
            transactionList.clear();
            transactionList.addAll(transactions);
            transactionTable.setItems(transactionList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void refresh(ActionEvent event) {
    	loadTransactionData();
    }
    @FXML
    private void handleSearchAction(ActionEvent event) {
        String searchTerm = searchTextField.getText();
        String filter = filterComboBox.getValue();

        try {
            List<Transaction> transactions = transactionHistoryDAO.searchTransactions(searchTerm, filter, userid);
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
    private void navigateToAddTransaction() {
        try {
            // Load the AddTransaction FXML
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/addTransaction.fxml"));
            Scene scene = new Scene(loader.load());

            // Create a new stage for the Add Transaction window
            Stage currentStage = (Stage) navigateToAddTransactionButton.getScene().getWindow();
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to load Add Transaction screen.", ButtonType.OK);
            alert.showAndWait();
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
    	Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/editTransaction.fxml"));
                Parent root = loader.load();

                // Pass the selected transaction to the EditTransactionController
                EditTransactionController controller = loader.getController();
                controller.setTransaction(selectedTransaction);

                // Set the scene
                Stage stage = new Stage();
                stage.setTitle("Edit Transaction");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current stage
                ((Stage) editButton.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to load edit screen.", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a transaction to edit.", ButtonType.OK);
            alert.showAndWait();
        }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initialize();
	}
}
