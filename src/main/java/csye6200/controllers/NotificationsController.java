package main.java.csye6200.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.csye6200.services.*;
import main.java.csye6200.models.*;

import java.util.List;

public class NotificationsController {

    @FXML
    private VBox notificationsContainer;

    private final NotificationsService notificationsService;

    public NotificationsController() {
        this.notificationsService = new NotificationsService();
    }

    @FXML
    public void initialize() {
    	
    	double highExpenseThreshold = 500;
    	
        List<Notifications> notifications = notificationsService.getAllNotifications(highExpenseThreshold);

        for (Notifications notification : notifications) {
            addNotification(notification);
        }
    }

    private void addNotification(Notifications notification) {
        HBox notificationBox = new HBox(10);
        notificationBox.setSpacing(10);
        notificationBox.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: lightgray; -fx-border-radius: 5;");

        ImageView icon = new ImageView();
        icon.setFitHeight(40);
        icon.setFitWidth(40);

        if ("CRITICAL".equalsIgnoreCase(notification.getType())) {
            icon.setImage(new Image(getClass().getResourceAsStream("/main/resources/images/critical-icon.png")));
        } else if ("MODERATE".equalsIgnoreCase(notification.getType())) {
            icon.setImage(new Image(getClass().getResourceAsStream("/main/resources/images/moderate-icon.png")));
        } else {
            icon.setImage(new Image(getClass().getResourceAsStream("/main/resources/images/neutral-icon.png")));
        }

        VBox textContent = new VBox();
        textContent.setSpacing(5);

        Label notificationTitle = new Label(notification.getTitle());
        notificationTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label notificationMessage = new Label(notification.getMessage());
        notificationMessage.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        textContent.getChildren().addAll(notificationTitle, notificationMessage);

        notificationBox.getChildren().addAll(icon, textContent);

        notificationsContainer.getChildren().add(notificationBox);
    }
}
