package main.java.csye6200.models;

import java.util.UUID;

public class Budget {

	private String budgetId;
    private double amount;
    private double remainingAmount;
    private String month;
    private int year;
    private String category;
	
	//Getter and Setter methods
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(String id) {
		this.budgetId = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	
	
    
    
}
