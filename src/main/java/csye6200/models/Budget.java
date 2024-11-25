package main.java.csye6200.models;

import java.util.Date;

public class Budget {

	private String budgetId;
    private double amount;
    private double remainingAmount;
    private Date startDate;
    private Date endDate;
    
    // Constructor
	public Budget(double amount, double remainingAmount, Date startDate, Date endDate) {
		super();
		this.amount = amount;
		this.remainingAmount = remainingAmount;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	//Getter and Setter methods
	public String getBudgetId() {
		return budgetId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    
    
}
