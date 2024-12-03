package main.java.csye6200.models;

public class MonthlySpending {
    private String categoryName;
    private double totalSpending;

    public MonthlySpending(String categoryName, double totalSpending) {
        this.categoryName = categoryName;
        this.totalSpending = totalSpending;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getTotalSpending() {
        return totalSpending;
    }
}
