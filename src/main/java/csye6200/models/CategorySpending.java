package main.java.csye6200.models;

public class CategorySpending {

    private final String categoryName;
    private final double totalSpending;

    public CategorySpending(String categoryName, double totalSpending) {
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
