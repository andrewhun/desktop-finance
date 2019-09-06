package com.andrewhun.finance.models;

import com.andrewhun.finance.util.NamedConstants;

import java.util.Date;

public class IncomeEntry extends StatementEntry {

    IncomeEntry(Integer id, String title, Double amount, Integer userId, Date time) {

        this.id = id;
        this.type = NamedConstants.INCOME;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.time = time;
    }

    IncomeEntry(String title, Double amount, Integer userId) {

        this.type = NamedConstants.INCOME;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
    }

    @Override protected Double calculateUserBalanceWhenEntryIsCreated(Double balance) {

        balance += amount;
        return balance;
    }

    @Override protected Double modifyUserBalanceWhenEntryIsDeleted(Double balance) {

        return (balance - amount);
    }
}
