/*
This file contains the BalanceUtil class, which is responsible for adjusting the user's balance based on
changes to other records (StatementEntries, primarily).
 */

package com.andrewhun.finance.util;

import com.andrewhun.finance.models.StatementEntry;
import static com.andrewhun.finance.util.NamedConstants.*;

public class BalanceUtil {

    public static Double adjustBalanceWhenStatementEntryIsEdited
            (Double balance, StatementEntry originalEntry, StatementEntry editedEntry) {

        balance = modifyBalanceWhenStatementEntryIsDeleted(balance, originalEntry);
        balance = calculateBalanceWhenStatementEntryIsCreated(balance, editedEntry);
        return balance;
    }

    public static Double calculateBalanceWhenStatementEntryIsCreated
            (Double balance, StatementEntry statementEntry) {

        if(statementEntry.hasType(EXPENSE)) {
            balance -= statementEntry.getAmount();
        }
        else if(statementEntry.hasType(INCOME) || statementEntry.hasType(BALANCE)) {

            balance += statementEntry.getAmount();
        }
        return balance;
    }

    public static Double modifyBalanceWhenStatementEntryIsDeleted(Double balance, StatementEntry statementEntry) {

        if (statementEntry.hasType(INCOME) || statementEntry.hasType(BALANCE)) {

            balance -= statementEntry.getAmount();
        }
        else if(statementEntry.hasType(EXPENSE)) {

            balance += statementEntry.getAmount();
        }
        return balance;
    }
}
