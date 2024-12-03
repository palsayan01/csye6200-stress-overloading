package main.java.csye6200.models;

public class Notifications {
	private String title;
    private String message;
    private String type; // "CRITICAL", "MODERATE", or "NORMAL"

    public Notifications(String title, String message, String type) {
    	this.title = title;
        this.message = message;
        this.type = type;
    }
    
    public String getTitle() {
    	return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
