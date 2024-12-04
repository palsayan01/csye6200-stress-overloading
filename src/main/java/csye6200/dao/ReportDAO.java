package main.java.csye6200.dao;

import main.java.csye6200.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
	
	private Connection connection;

    public ReportDAO() throws SQLException, ClassNotFoundException {
        connection = new DatabaseConnect().getConnection();
    }
    
    public List<MonthlySpending> getMonthlySpending() {
        List<MonthlySpending> spendings = new ArrayList<>();
        String query = """
            SELECT 
                c.CATEGORY_NAME, 
                SUM(t.AMOUNT) AS TOTAL_SPENDING
            FROM 
                TRANSACTIONS t
            INNER JOIN 
                CATEGORY c ON t.CATEGORY_ID = c.CATEGORY_ID
            WHERE 
                EXTRACT(MONTH FROM t.TRANSACTION_DATE) = EXTRACT(MONTH FROM CURRENT_DATE)
            GROUP BY 
                c.CATEGORY_NAME
            ORDER BY 
                TOTAL_SPENDING DESC
        """;

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
        String query = """
            SELECT 
                TO_CHAR(t.TRANSACTION_DATE, 'Month') AS MONTH,
                SUM(CASE WHEN t.TRANSACTION_TYPE = 'INCOME' THEN t.AMOUNT ELSE 0 END) AS TOTAL_INCOME,
                SUM(CASE WHEN t.TRANSACTION_TYPE = 'EXPENSE' THEN t.AMOUNT ELSE 0 END) AS TOTAL_EXPENSES,
                (SUM(CASE WHEN t.TRANSACTION_TYPE = 'INCOME' THEN t.AMOUNT ELSE 0 END) - 
                 SUM(CASE WHEN t.TRANSACTION_TYPE = 'EXPENSE' THEN t.AMOUNT ELSE 0 END)) AS NET_SAVINGS
            FROM 
                TRANSACTIONS t
            WHERE 
                EXTRACT(YEAR FROM TO_DATE( t.TRANSACTION_DATE, 'YY-Mon-DD')) = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY 
                TO_CHAR(t.TRANSACTION_DATE, 'Month')
            ORDER BY 
                TO_DATE(TO_CHAR(t.TRANSACTION_DATE, 'Month'), 'Month')
        """;

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
        String query = """
            SELECT 
                c.CATEGORY_NAME, 
                SUM(t.AMOUNT) AS TOTAL_SPENDING
            FROM 
                TRANSACTIONS t
            INNER JOIN 
                CATEGORY c ON t.CATEGORY_ID = c.CATEGORY_ID
            WHERE 
                t.TRANSACTION_TYPE = 'EXPENSE'
            GROUP BY 
                c.CATEGORY_NAME
            ORDER BY 
                TOTAL_SPENDING DESC
            FETCH FIRST 5 ROWS ONLY
        """;

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
