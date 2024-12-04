package main.java.csye6200.dao;

import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.Category;
import main.java.csye6200.models.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryDAO {

    private Connection connection;

    public TransactionHistoryDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all transactions
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        ResultSet resultSet = null;

        try {
        	System.out.print(query);
            PreparedStatement statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                		resultSet.getString("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("transaction_date").toLocalDate(),
                        resultSet.getString("category_id"),
                        TransactionType.valueOf(resultSet.getString("transaction_type"))
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
    public List<Transaction> searchTransactions(String searchTerm, String filter) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE description LIKE ?";
        if (filter != null && !filter.equals("All")) {
            query += " AND type = ?";
        }

        ResultSet resultSet = null;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm + "%");
            if (filter != null && !filter.equals("All")) {
                statement.setString(2, filter.toUpperCase());
            }
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                		resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("transaction_date").toLocalDate(),
                        resultSet.getString("category_id"),
                        TransactionType.valueOf(resultSet.getString("transaction_type"))
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
