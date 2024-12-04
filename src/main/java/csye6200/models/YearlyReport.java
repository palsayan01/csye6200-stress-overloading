package main.java.csye6200.models;

public class YearlyReport {
    private String month;
    private double totalIncome;
    private double totalExpenses;
    private double netSavings;

    public YearlyReport(String month, double totalIncome, double totalExpenses, double netSavings) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netSavings = netSavings;
    }

    public String getMonth() {
        return month;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getNetSavings() {
        return netSavings;
    }
}
