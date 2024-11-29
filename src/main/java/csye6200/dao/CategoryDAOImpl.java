package main.java.csye6200.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.csye6200.models.Budget;
import main.java.csye6200.models.Category;

public class CategoryDAOImpl {

	private DatabaseConnect dbConnection;
	private Connection con;
	private int result;
	private ResultSet rs;

	public CategoryDAOImpl(DatabaseConnect dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	public int createCategory(Category cat) throws ClassNotFoundException {
		try {
			con = dbConnection.getConnection();
			String query = "INSERT INTO CATEGORY VALUES (?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, cat.getCategoryId());
			st.setString(2, cat.getCategoryName());
			result = st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result>0 ? 1:0;
		
	}
	
	public ResultSet getCategory() throws ClassNotFoundException {
		try {
			con = dbConnection.getConnection();
			String query = "SELECT CATEGORY_NAME FROM CATEGORY";
			PreparedStatement st = con.prepareStatement(query);
			rs = st.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
		
	}
}
