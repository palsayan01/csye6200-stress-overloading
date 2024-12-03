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
            	System.out.print(resultSet.getInt("id"));
                System.out.print("disjdijs");
                System.out.print(resultSet.getInt("date"));
                Transaction transaction = new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("category"),
                        TransactionType.valueOf(resultSet.getString("type"))
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
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("category"),
                        TransactionType.valueOf(resultSet.getString("type"))
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

    // Delete a transaction by its ID
    public boolean deleteTransaction(int transactionId) throws SQLException {
        boolean isDeleted = false;
        String query = "DELETE FROM transactions WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, transactionId);
            int rowsAffected = statement.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }
}
