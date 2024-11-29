package main.java.csye6200.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.csye6200.models.Budget;
import main.java.csye6200.models.Goal;

public class GoalDAOImpl {
	private DatabaseConnect dbConnection;
	private Connection con;
	private int result;
	private ResultSet rs;

	public GoalDAOImpl(DatabaseConnect dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	public int createGoal(Goal goal) throws ClassNotFoundException {
		try {
			con = dbConnection.getConnection();
			String query = "INSERT INTO GOAL VALUES (?, ?, ?,?)";
			PreparedStatement st = con.prepareStatement(query);
			java.sql.Date sqlDate = java.sql.Date.valueOf(goal.getDueDate());
			st.setString(1, goal.getGoalId());
			st.setString(2, goal.getGoalName());
			st.setDouble(3, goal.getTargetAmount());
			st.setDate(4, sqlDate);
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result>0 ? 1:0;
		
	}

}
