package main.java.csye6200.dao;

import main.java.csye6200.models.Transaction;

import java.sql.*;
import java.time.LocalDate;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO() throws SQLException, ClassNotFoundException {
        connection = new DatabaseConnect().getConnection();
    }

    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (id, description, amount, transaction_date, category_id, transaction_type) VALUES (?,?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        	stmt.setString(1,transaction.getId());
            stmt.setString(2, transaction.getDescription());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setDate(4, Date.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getCategory());  // Category is now a String
            stmt.setString(6, transaction.getType().toString());
            System.out.print(stmt);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
