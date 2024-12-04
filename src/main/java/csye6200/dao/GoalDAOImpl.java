package main.java.csye6200.dao;

import java.sql.CallableStatement;
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
			String query = "INSERT INTO GOAL VALUES (?, ?, ?,?,?,?)";
			PreparedStatement st = con.prepareStatement(query);
			java.sql.Date sqlDate = java.sql.Date.valueOf(goal.getDueDate());
			st.setString(1, goal.getGoalId());
			st.setString(2, goal.getGoalName());
			st.setDouble(3, goal.getTargetAmount());
			st.setDate(4, sqlDate);
			st.setInt(5, 0);
			st.setString(6, "In Progress");
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result > 0 ? 1 : 0;

	}

	public int checkGoalAchieved(String goalName) throws ClassNotFoundException {
		try {
			System.out.println("Inside proc call");
			con = dbConnection.getConnection();
			String query = "{call CheckGoalAchievement(?)}";
			CallableStatement st = con.prepareCall(query);
			st.setString(1, goalName);
			st.execute();
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result > 0 ? 1 : 0;

	}

	public ResultSet getProgress(String goalName) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		try {
			con = dbConnection.getConnection();
			String query = "SELECT PERCENT_ACHIEVED FROM GOAL WHERE GOAL_NAME=? ";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, goalName);
			rs = st.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
		
	}
	
	public ResultSet goals() throws ClassNotFoundException {
		try {
			con = dbConnection.getConnection();
			String query = "SELECT GOAL_NAME FROM GOAL";
			PreparedStatement st = con.prepareStatement(query);
			rs = st.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;

}
}
