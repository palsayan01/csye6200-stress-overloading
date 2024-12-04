package main.java.csye6200.models;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private String id;                   
    private String description;        
    private double amount;             
    private LocalDate transactionDate; 
    private String category;        
    private TransactionType type;   
    private String userid;
    public static int count = 0;
    // Constructor
    public Transaction(String description, double amount, LocalDate transactionDate, String category, TransactionType type, String userid) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.category = category;
        this.type = type;
        this.userid = userid;
    }
    public Transaction(String id, String description, double amount, LocalDate transactionDate, String category, TransactionType type, String userid) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.category = category;
        this.type = type;
        this.userid = userid;
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
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
