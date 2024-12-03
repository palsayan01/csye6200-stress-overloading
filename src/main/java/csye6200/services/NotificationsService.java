package main.java.csye6200.services;

import main.java.csye6200.dao.*;
import main.java.csye6200.models.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsService {

    private NotificationsDAO notificationsDAO;

    public List<Notifications> getAllNotifications(double highExpenseThreshold) {
    	try {
			notificationsDAO = new NotificationsDAO();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
        List<Notifications> notifications = new ArrayList<>();
        notifications.addAll(notificationsDAO.getBudgetThresholdNotifications());
        notifications.addAll(notificationsDAO.getHighExpenseAlerts(highExpenseThreshold));
        return notifications;
    }
}
