package com.andrewhun.finance.models;

import com.andrewhun.finance.util.NamedConstants;

import java.util.Date;

public class ExpenseEntry extends StatementEntry {

    ExpenseEntry(Integer id, String title, Double amount, Integer userId, Date time) {

        this.id = id;
        this.type = NamedConstants.EXPENSE;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.time = time;
    }

    ExpenseEntry(String title, Double amount, Integer userId) {

        this.type = NamedConstants.EXPENSE;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
    }

    @Override
    protected Double calculateUserBalanceWhenEntryIsCreated(Double balance) {

        return balance - amount;
    }

    @Override
    protected Double modifyUserBalanceWhenEntryIsDeleted(Double balance) {

        return balance + amount;
    }
}
