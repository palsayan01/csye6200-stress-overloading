package main.java.csye6200.dao;

import main.java.csye6200.models.Transaction;
import main.java.csye6200.models.TransactionType;

import java.sql.*;
import java.time.LocalDate;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnect.getInstance().getConnection();
    }

    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (id, description, amount, transaction_date, category_id, transaction_type, userid) VALUES (?,?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        	stmt.setString(1,transaction.getId());
            stmt.setString(2, transaction.getDescription());
            stmt.setDouble(3, transaction.getAmount());
            System.out.println(transaction.getDate());
            stmt.setDate(4, java.sql.Date.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getCategory());  // Category is now a String
            stmt.setString(6, transaction.getType().toString());
            stmt.setString(7,  transaction.getUserid());
            System.out.print(stmt);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String query = "UPDATE transactions SET description = ?, amount = ?, category_id = ?, transaction_date = ?, transaction_type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, transaction.getDescription());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setString(3, transaction.getCategory());
//            stmt.setDate(4, java.sql.Date.valueOf(transaction.getDate()));
            stmt.setDate(4, java.sql.Date.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getType().name());
            stmt.setString(6, transaction.getId());
            return stmt.executeUpdate() > 0;
        }
    }
}
