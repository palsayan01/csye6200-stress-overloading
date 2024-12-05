package main.java.csye6200.dao;

import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.Category;
import main.java.csye6200.models.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryDAO {

    private Connection connection;

    public TransactionHistoryDAO() throws SQLException, ClassNotFoundException {
        this.connection = DatabaseConnect.getInstance().getConnection();
    }

    // Fetch all transactions
    public List<Transaction> getAllTransactions(String userid) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE userid = ?";
        
        ResultSet resultSet = null;

        try {
        	  
        	System.out.print(query);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userid);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                		resultSet.getString("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("transaction_date").toLocalDate(),
                        resultSet.getString("category_id"),
                        TransactionType.valueOf(resultSet.getString("transaction_type")),
                        userid
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return transactions;
    }

    // Search and filter transactions by description and type
    public List<Transaction> searchTransactions(String searchTerm, String filter, String userid) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE userid = ? AND description LIKE ?";
        if (filter != null && !filter.equalsIgnoreCase("All")) {
            query += " AND transaction_type = ?";
        }

        ResultSet resultSet = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userid); // Set the userid parameter
            statement.setString(2, "%" + searchTerm + "%"); // Set the description parameter

            if (filter != null && !filter.equalsIgnoreCase("All")) {
                statement.setString(3, filter.toUpperCase()); // Set the transaction_type parameter
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                    resultSet.getString("description"),
                    resultSet.getDouble("amount"),
                    resultSet.getDate("transaction_date").toLocalDate(),
                    resultSet.getString("category_id"),
                    TransactionType.valueOf(resultSet.getString("transaction_type")),
                    resultSet.getString("userid") // Use the `userid` from the database
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log exception (Consider using a logging framework)
        } finally {
            if (resultSet != null) {
                resultSet.close(); // Close the ResultSet
            }
        }

        return transactions;
    }

    // update transaction on edit
    


    // Delete a transaction by its ID
    public boolean deleteTransaction(String string) throws SQLException {
        boolean isDeleted = false;
        String query = "DELETE FROM transactions WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, string);
            System.out.println(statement);
            int rowsAffected = statement.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }
}
