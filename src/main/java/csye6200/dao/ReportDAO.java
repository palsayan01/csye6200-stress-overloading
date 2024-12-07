package main.java.csye6200.dao;

import main.java.csye6200.models.*;
import main.java.csye6200.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
	
	private Connection connection;

    public ReportDAO() throws SQLException, ClassNotFoundException {
        this.connection = DatabaseConnect.getInstance().getConnection();
    }
    
    public List<MonthlySpending> getMonthlySpending() {
        List<MonthlySpending> spendings = new ArrayList<>();
        String query = String.format("""
            SELECT 
                c.CATEGORY_NAME, 
                SUM(t.AMOUNT) AS TOTAL_SPENDING
            FROM 
                TRANSACTIONS t
            INNER JOIN 
                CATEGORY c ON t.CATEGORY_ID = c.CATEGORY_ID
            WHERE 
        		t.USERID = '%s' AND
        		t.TRANSACTION_TYPE = 'EXPENSE' AND
                EXTRACT(MONTH FROM t.TRANSACTION_DATE) = EXTRACT(MONTH FROM CURRENT_DATE)
            GROUP BY 
                c.CATEGORY_NAME
            ORDER BY 
                TOTAL_SPENDING DESC
        """, SessionManager.getInstance().getUserId());

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                spendings.add(new MonthlySpending(
                        resultSet.getString("CATEGORY_NAME"),
                        resultSet.getDouble("TOTAL_SPENDING")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spendings;
    }

    public List<YearlyReport> getYearlyIncomeExpense() {
        List<YearlyReport> reports = new ArrayList<>();
        String query = String.format("""
            SELECT 
                TO_CHAR(TO_DATE(t.TRANSACTION_DATE, 'YY-MM-DD'), 'Month') AS MONTH,
                SUM(CASE WHEN t.TRANSACTION_TYPE = 'INCOME' THEN t.AMOUNT ELSE 0 END) AS TOTAL_INCOME,
                SUM(CASE WHEN t.TRANSACTION_TYPE = 'EXPENSE' THEN t.AMOUNT ELSE 0 END) AS TOTAL_EXPENSES,
                (SUM(CASE WHEN t.TRANSACTION_TYPE = 'INCOME' THEN t.AMOUNT ELSE 0 END) - 
                 SUM(CASE WHEN t.TRANSACTION_TYPE = 'EXPENSE' THEN t.AMOUNT ELSE 0 END)) AS NET_SAVINGS
            FROM 
                TRANSACTIONS t
            WHERE 
                t.USERID = '%s' AND
                EXTRACT(YEAR FROM TO_DATE(t.TRANSACTION_DATE, 'YY-MM-DD')) = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY 
                TO_CHAR(TO_DATE(t.TRANSACTION_DATE, 'YY-MM-DD'), 'Month'),
                EXTRACT(MONTH FROM TO_DATE(t.TRANSACTION_DATE, 'YY-MM-DD'))
            ORDER BY 
                EXTRACT(MONTH FROM TO_DATE(t.TRANSACTION_DATE, 'YY-MM-DD'))
        """, SessionManager.getInstance().getUserId());

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reports.add(new YearlyReport(
                        resultSet.getString("MONTH").trim(),
                        resultSet.getDouble("TOTAL_INCOME"),
                        resultSet.getDouble("TOTAL_EXPENSES"),
                        resultSet.getDouble("NET_SAVINGS")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }
    public List<CategorySpending> getTopSpendingCategories() {
        List<CategorySpending> categories = new ArrayList<>();
        String query = String.format("""
            SELECT 
                c.CATEGORY_NAME, 
                SUM(t.AMOUNT) AS TOTAL_SPENDING
            FROM 
                TRANSACTIONS t
            INNER JOIN 
                CATEGORY c ON t.CATEGORY_ID = c.CATEGORY_ID
            WHERE 
        		t.USERID = '%s' AND
                t.TRANSACTION_TYPE = 'EXPENSE'
            GROUP BY 
                c.CATEGORY_NAME
            ORDER BY 
                TOTAL_SPENDING DESC
            FETCH FIRST 5 ROWS ONLY
        """, SessionManager.getInstance().getUserId());

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(new CategorySpending(
                        resultSet.getString("CATEGORY_NAME"),
                        resultSet.getDouble("TOTAL_SPENDING")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}
