package main.java.csye6200.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Goal {
	private String goalId;
    private String goalName;
    private double targetAmount;
//    private double currentAmount;
    private LocalDate dueDate;

	
	//Getter and Setter methods
	public String getGoalId() {
		return goalId;
	}
	

	public void setGoalId() {
		this.goalId = UUID.randomUUID().toString();
	}


	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
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

//	public double getCurrentAmount() {
//		return currentAmount;
//	}
//
//	public void setCurrentAmount(double currentAmount) {
//		this.currentAmount = currentAmount;
//	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	
	
    

    
    

}
