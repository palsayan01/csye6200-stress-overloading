package main.java.csye6200.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.csye6200.models.Budget;

public class BudgetDAOImpl {
	
	private DatabaseConnect dbConnection;
	private Connection con;
	private int result;
	private ResultSet rs;

	public BudgetDAOImpl(DatabaseConnect dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	public int createBudget(Budget budget) throws ClassNotFoundException {
		try {
			con = dbConnection.getConnection();
			String query = "INSERT INTO BUDGET VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, budget.getBudgetId());
			st.setDouble(2, budget.getAmount());
			st.setDouble(3, budget.getRemainingAmount());
			st.setString(4, budget.getMonth());
			st.setInt(5, budget.getYear());
			st.setString(6, budget.getCategory().getCategoryId());
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result>0 ? 1:0;
		
	}
	
	public ResultSet getBudgetDetails(String month, int year) throws ClassNotFoundException {
		try {
			con = dbConnection.getConnection();
			String query = "select amount, remaining_amount, cat.category_name from budget bud\r\n"
					+ "left join category cat\r\n"
					+ "on bud.category_id = cat.category_id\r\n"
					+ "where month=? and year=?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, month);
			st.setInt(2, year);
			rs = st.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
		
	}
	

}
