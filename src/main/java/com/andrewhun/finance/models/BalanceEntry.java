package com.andrewhun.finance.models;

import com.andrewhun.finance.util.NamedConstants;

import java.util.Date;

public class BalanceEntry extends StatementEntry {


    BalanceEntry(Integer id, String title, Double amount, Integer userId, Date time) {

        this.id = id;
        this.type = NamedConstants.BALANCE;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.time = time;
    }

    BalanceEntry(String title, Double amount, Integer userId) {

        this.type = NamedConstants.BALANCE;
        this.title = title;
        this.amount = amount;
        this.userId = userId;
    }

    @Override public void delete() {

        // Deleting balance entries is not supported functionality
    }

    @Override protected Double calculateUserBalanceWhenEntryIsCreated(Double balance) {

        return balance;
    }

    @Override protected Double adjustUserBalanceWhenEntryIsEdited(Double balance, StatementEntry
            originalEntry, StatementEntry editedEntry) {

        balance -= originalEntry.getAmount();
        balance += editedEntry.getAmount();
        return balance;
    }

    @Override
    protected Double modifyUserBalanceWhenEntryIsDeleted(Double balance) {

        return null;
    }
}
