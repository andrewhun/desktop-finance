/*
* This file contains the StatementEntry class. An instance of this class
* corresponds to an entry in the Statement entries table of the database.
 */

package com.andrewhun.finance.models;

import java.util.Date;

public class StatementEntry implements ModifiableEntry {

    private Integer id;
    private String type;
    private String title;
    private Double amount;
    private Integer userId;
    private Date time;

    public StatementEntry (Integer id, String type, String title, Double amount, Integer userId, Date time){

        this.id = id;
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.time = time;
    }

    public Boolean hasType(String type) {

        return this.getType().equals(type);
    }

    public static StatementEntry createIncompleteEntry(String type, String title, Double amount, Integer userId) {

        return new StatementEntry(type, title, amount, userId);
    }

    private StatementEntry(String type, String title, Double amount, Integer userId) {

        this.type = type;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
    }

    public StatementEntry() {}

    public Integer getId() {

        return id;
    }

    public void setId(Integer newId) {

        id = newId;
    }

    public String getType() {

        return type;
    }

    public void setType(String newType) {

        type = newType;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String newTitle) {

        title = newTitle;
    }

    public Double getAmount() {

        return amount;
    }

    public void setAmount(Double newAmount) {

        amount = newAmount;
    }

    public Integer getUserId() {

        return userId;
    }

    public void setUserId(Integer newUserId) {

        userId = newUserId;
    }

    public Date getTime() {

        return time;
    }

    public void setTime(Date newTime) {

        time = newTime;
    }

    @Override
    public String toString() {

        return (type + " of " + amount + " with title " + title);
    }
}