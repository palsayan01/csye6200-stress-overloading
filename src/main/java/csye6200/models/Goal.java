package main.java.csye6200.models;

import java.util.Date;

public class Goal {
	private String goalId;
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private Date dueDate;
    
    //Constructor
	public Goal(String goalName, double targetAmount, double currentAmount, Date dueDate) {
		super();
		this.goalName = goalName;
		this.targetAmount = targetAmount;
		this.currentAmount = currentAmount;
		this.dueDate = dueDate;
	}
	
	//Getter and Setter methods
	public String getGoalId() {
		return goalId;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(double targetAmount) {
		this.targetAmount = targetAmount;
	}

	public double getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(double currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
    

    
    

}
