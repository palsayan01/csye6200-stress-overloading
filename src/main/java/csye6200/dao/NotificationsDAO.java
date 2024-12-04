package main.java.csye6200.dao;

import main.java.csye6200.models.*;
import main.java.csye6200.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDAO {

	private Connection connection;

    public NotificationsDAO() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnect.getInstance().getConnection();
    }

    public List<Notifications> getBudgetThresholdNotifications() {
        List<Notifications> notifications = new ArrayList<>();
        String query = String.format("""
            SELECT 
                c.CATEGORY_NAME, 
                b.AMOUNT, 
                b.REMAINING_AMOUNT,
                (b.AMOUNT - b.REMAINING_AMOUNT) / b.AMOUNT * 100 AS PERCENT_SPENT
            FROM 
                BUDGET b
            INNER JOIN
			    CATEGORY c ON b.CATEGORY_ID = c.CATEGORY_ID
			WHERE
        		b.USERID = '%s' AND
			    UPPER(TRIM(b.MONTH)) = UPPER(TRIM(TO_CHAR(CURRENT_DATE, 'Month')))
			    AND b.YEAR = EXTRACT(YEAR FROM CURRENT_DATE)
        """, SessionManager.getInstance().getUserId());

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String category = resultSet.getString("CATEGORY_NAME");
                double percentSpent = resultSet.getDouble("PERCENT_SPENT");
                double remaining = resultSet.getDouble("REMAINING_AMOUNT");

                if (percentSpent >= 100) {
                    notifications.add(new Notifications("Exceeded Budget",
                        "You have exceeded your budget for " + category + 
                        " by $" + (-remaining) + ".", "CRITICAL"));
                } else if (percentSpent >= 90) {
                    notifications.add(new Notifications("Almost reached budget limit",
                        "You’re close to your budget limit for " + category + 
                        ". Only $" + remaining + " left!", "CRITICAL"));
                } else if (percentSpent >= 75) {
                    notifications.add(new Notifications("Approaching budget limit",
                        "Heads up! You have spent 75% of your budget for " + category + ".", "MODERATE"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public List<Notifications> getHighExpenseAlerts(double threshold) {
        List<Notifications> notifications = new ArrayList<>();
        String query = String.format("""
            SELECT 
                t.DESCRIPTION, 
                c.CATEGORY_NAME, 
                t.AMOUNT 
            FROM 
                TRANSACTIONS t
            INNER JOIN 
                CATEGORY c ON t.CATEGORY_ID = c.CATEGORY_ID
            WHERE 
        		t.USERID = '%s' AND    
                t.AMOUNT > ?
        """, SessionManager.getInstance().getUserId());

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, threshold);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                notifications.add(new Notifications("High Expense Alert",
                    "Your transaction of $" + resultSet.getDouble("AMOUNT") +
                    " in " + resultSet.getString("CATEGORY_NAME") + " exceeds the threshold.", "NORMAL"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
