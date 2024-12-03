package main.java.csye6200.models;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private String id;                    // Maps to the ID column in the table
    private String description;        // Maps to the DESCRIPTION column
    private double amount;             // Maps to the AMOUNT column
    private LocalDate transactionDate; // Renamed to match the TRANSACTION_DATE column
    private String category;         // Represents the foreign key CATEGORY_ID
    private TransactionType type;      // Represents the TYPE column as an enum
    public static int count = 0;
    // Constructor
    public Transaction(String description, double amount, LocalDate transactionDate, String category, TransactionType type) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.category = category;
        this.type = type;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", description=" + description + ", amount=" + amount + ", transactionDate=" + transactionDate
                + ", category=" + category + ", type=" + type + "]";
    }
}
