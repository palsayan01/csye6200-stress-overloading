package main.java.csye6200.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AlertUtils {
	
	public static void showAlert(Alert.AlertType alertType, String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(alertType, message, ButtonType.OK);
			
			alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
	
		    // Prevent interactions with the parent window while alert is active
		    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		    alertStage.setAlwaysOnTop(true);
		    alertStage.setResizable(false);
	
		    // Block until the user responds to the alert
		    alert.show();
			});
	}

	public static void showAlert(Alert.AlertType alertType, String message, Stage owner) {
		Platform.runLater(() -> {
			Alert alert = new Alert(alertType, message, ButtonType.OK);
			
			if (owner != null) {
	            alert.initOwner(owner);
	        }
			
			alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Information");
	
		    // Prevent interactions with the parent window while alert is active
		    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		    alertStage.setAlwaysOnTop(true);
		    alertStage.setResizable(false);
	
		    // Block until the user responds to the alert
		    alert.show();
			});
	}
}