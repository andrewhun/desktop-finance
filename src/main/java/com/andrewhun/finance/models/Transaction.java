/*
 * This file contains the Transaction class. An instance of this class
 * corresponds to an entry in the Transactions table of the database.
 */

package com.andrewhun.finance.models;

import java.util.Date;

public class Transaction {

    private Integer id;
    private Integer userId;
    private String type;
    private String stock;
    private Double price;
    private Integer shares;
    private Date time;

    public Transaction(Integer id, Integer userId, String type,
                       String stock, Double price, Integer shares, Date time) {

        this.id = id;
        this.userId = userId;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.shares = shares;
        this.time = time;
    }

    public static Transaction createIncompleteTransaction
            (Integer userId, String type, String stock, Double price, Integer shares) {

        return new Transaction(userId, type, stock, price, shares);
    }

    private Transaction(Integer userId, String type, String stock, Double price, Integer shares) {

        this.userId = userId;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.shares = shares;
    }

    // Temporary, used for testing TransactionTableProcedures. Remove once that class is complete!
    public Transaction() {}

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public Integer getUserId() {

        return userId;
    }

    public void setUserId(Integer userId) {

        this.userId = userId;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getStock() {

        return stock;
    }

    public void setStock(String stock) {

        this.stock = stock;
    }

    public Double getPrice() {

        return price;
    }

    public void setPrice(Double price) {

        this.price = price;
    }

    public Integer getShares() {

        return shares;
    }

    public void setShares(Integer shares) {

        this.shares = shares;
    }

    public Date getTime() {

        return time;
    }

    public void setTime(Date time) {

        this.time = time;
    }
}
