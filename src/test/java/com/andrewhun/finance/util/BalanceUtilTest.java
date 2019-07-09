package com.andrewhun.finance.util;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.StatementEntry;
import static com.andrewhun.finance.util.NamedConstants.*;

class BalanceUtilTest {


    @Test void testCalculateBalanceWhenExpenseEntryIsCreated() {

        double originalBalance = DEFAULT_BALANCE;
        StatementEntry rentEntry =
                StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);

        double actualBalance = BalanceUtil.calculateBalanceWhenStatementEntryIsCreated(originalBalance, rentEntry);
        Assertions.assertEquals(DEFAULT_BALANCE - FIVE_HUNDRED, actualBalance);
    }

    @Test void testCalculateBalanceWhenIncomeEntryIsCreated() {

        double originalBalance = FIVE_HUNDRED;

        StatementEntry workEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);

        double actualBalance = BalanceUtil.calculateBalanceWhenStatementEntryIsCreated(originalBalance, workEntry);
        Assertions.assertEquals(FIVE_HUNDRED + THREE_HUNDRED, actualBalance);
    }

    @Test void testModifyBalanceWhenExpenseEntryIsDeleted() {

        double originalBalance = FIVE_HUNDRED;

        StatementEntry rentEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);

        double actualBalance = BalanceUtil.modifyBalanceWhenStatementEntryIsDeleted(originalBalance, rentEntry);
        Assertions.assertEquals(FIVE_HUNDRED + FIVE_HUNDRED, actualBalance);
    }

    @Test void testModifyBalanceWhenIncomeEntryIsDeleted() {

        double originalBalance = FIVE_HUNDRED;

        StatementEntry workEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);

        double actualBalance = BalanceUtil.modifyBalanceWhenStatementEntryIsDeleted(originalBalance, workEntry);
        Assertions.assertEquals(FIVE_HUNDRED - THREE_HUNDRED, actualBalance);
    }

    @Test void testAdjustBalanceWhenBalanceEntryAmountIsChanged() {

        double originalBalance = DEFAULT_BALANCE;

        StatementEntry balanceEntry =
                StatementEntry.createIncompleteEntry(BALANCE, STARTING_BALANCE, DEFAULT_BALANCE, ID);

        StatementEntry editedBalanceEntry =
                StatementEntry.createIncompleteEntry(BALANCE, STARTING_BALANCE, FIVE_HUNDRED, ID);

        double actualBalance =
                BalanceUtil.adjustBalanceWhenStatementEntryIsEdited
                        (originalBalance, balanceEntry, editedBalanceEntry);

        Assertions.assertEquals((double)FIVE_HUNDRED, actualBalance);
    }

    @Test void testAdjustBalanceWhenIncomeEntryIsChangedToExpense() {

        double originalBalance = DEFAULT_BALANCE;

        StatementEntry workEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);

        StatementEntry expenseEntry = StatementEntry.createIncompleteEntry(EXPENSE, WORK, THREE_HUNDRED, ID);

        double actualBalance =
                BalanceUtil.adjustBalanceWhenStatementEntryIsEdited(originalBalance, workEntry, expenseEntry);

        Assertions.assertEquals(DEFAULT_BALANCE - (THREE_HUNDRED * 2), actualBalance);
    }

    @Test void testAdjustBalanceWhenIncomeEntryAmountIsChanged() {

        double originalBalance = FIVE_HUNDRED;

        StatementEntry workEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);

        StatementEntry increasedAmountEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, FIVE_HUNDRED, ID);

        double actualBalance =
                BalanceUtil.adjustBalanceWhenStatementEntryIsEdited(originalBalance, workEntry, increasedAmountEntry);

        Assertions.assertEquals(FIVE_HUNDRED - THREE_HUNDRED + FIVE_HUNDRED, actualBalance);
    }

    @Test void testAdjustBalanceWhenExpenseEntryIsChangedToIncome() {

        double originalBalance = THREE_HUNDRED;

        StatementEntry rentEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);
        StatementEntry incomeEntry = StatementEntry.createIncompleteEntry(INCOME, RENT, FIVE_HUNDRED, ID);

        double actualBalance =
                BalanceUtil.adjustBalanceWhenStatementEntryIsEdited(originalBalance, rentEntry, incomeEntry);

        Assertions.assertEquals(THREE_HUNDRED + (FIVE_HUNDRED * 2), actualBalance);
    }

    @Test void testAdjustBalanceWhenExpenseEntryAmountIsChanged() {

        double originalBalance = THREE_HUNDRED;

        StatementEntry rentEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);
        StatementEntry reducedAmountEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, THREE_HUNDRED, ID);

        double actualBalance =
                BalanceUtil.adjustBalanceWhenStatementEntryIsEdited(originalBalance, rentEntry, reducedAmountEntry);

        Assertions.assertEquals(THREE_HUNDRED + FIVE_HUNDRED - THREE_HUNDRED, actualBalance);
    }
}
