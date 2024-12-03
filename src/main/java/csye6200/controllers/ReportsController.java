package main.java.csye6200.controllers;

import main.java.csye6200.dao.ReportDAO;
import main.java.csye6200.dao.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import main.java.csye6200.models.*;

import java.sql.SQLException;
import java.util.List;

public class ReportsController {

    @FXML private PieChart monthlySpendingPieChart;
    @FXML private PieChart topSpendingPieChart;
    @FXML private BarChart<String, Number> yearlyBarChart;
    @FXML private LineChart<String, Number> yearlyLineChart;
    @FXML private CategoryAxis barChartXAxis;
    @FXML private NumberAxis barChartYAxis;
    @FXML private CategoryAxis lineChartXAxis;
    @FXML private NumberAxis lineChartYAxis;

    private ReportDAO reportDAO;

    @FXML
    public void initialize() {
    	try {
			reportDAO = new ReportDAO();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
        loadMonthlySpendingData();
        loadYearlyIncomeExpenseData();
        loadTopSpendingCategoriesData();
    }

    private void loadMonthlySpendingData() {
        List<MonthlySpending> monthlySpendings = reportDAO.getMonthlySpending();
        for (MonthlySpending spending : monthlySpendings) {
            monthlySpendingPieChart.getData().add(
                new PieChart.Data(spending.getCategoryName(), spending.getTotalSpending())
            );
        }
    }

    private void loadYearlyIncomeExpenseData() {
        List<YearlyReport> yearlyReports = reportDAO.getYearlyIncomeExpense();
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");
        XYChart.Series<String, Number> netSavingsSeries = new XYChart.Series<>();
        netSavingsSeries.setName("Net Savings");

        for (YearlyReport report : yearlyReports) {
            incomeSeries.getData().add(new XYChart.Data<>(report.getMonth(), report.getTotalIncome()));
            expenseSeries.getData().add(new XYChart.Data<>(report.getMonth(), report.getTotalExpenses()));
            netSavingsSeries.getData().add(new XYChart.Data<>(report.getMonth(), report.getNetSavings()));
        }

        yearlyBarChart.getData().addAll(incomeSeries, expenseSeries);
        yearlyLineChart.getData().addAll(netSavingsSeries);
    }

    private void loadTopSpendingCategoriesData() {
        List<CategorySpending> topCategories = reportDAO.getTopSpendingCategories();
        for (CategorySpending category : topCategories) {
            topSpendingPieChart.getData().add(
                new PieChart.Data(category.getCategoryName(), category.getTotalSpending())
            );
        }
    }
}
