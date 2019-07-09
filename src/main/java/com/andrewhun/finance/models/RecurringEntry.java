/*
* This file contains the RecurringEntry class.
* An instance of this class corresponds to
* an entry in the Recurring entries table of the database.
 */

package com.andrewhun.finance.models;

import java.util.Date;

public class RecurringEntry implements ModifiableEntry {

    private Integer id;
    private String type;
    private String title;
    private Double amount;
    private Integer userId;
    private Date time;
    private String frequency;

    public RecurringEntry(Integer id, String type, String title, Double amount, Integer userId, Date time,
                   String frequency) {

        this.id = id;
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.time = time;
        this.frequency = frequency;
    }

    public static RecurringEntry createIncompleteEntry(String type, String title,
                                                       Double amount, Integer userId, String frequency) {

        return new RecurringEntry(type, title, amount, userId, frequency);
    }

    private RecurringEntry(String type, String title, Double amount, Integer userId, String frequency) {

        this.type = type;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.frequency = frequency;
    }

    public RecurringEntry() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {

        return (type + " of " + amount + " with title " + title + " recurring " + frequency + ".");
    }
}
