package com.andrewhun.finance.models;

import static org.junit.jupiter.api.Assertions.*;

import com.andrewhun.finance.util.NamedConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

class TransactionTest {

    private Transaction transaction;
    private Date originalTime = new Date();

    private final String BUY = "Buy";
    private final String NETFLIX = "NFLX";

    private final String SELL = "Sell";
    private final String APPLE = "AAPL";

    private final Integer TEN = 10;
    private final Integer FIVE = 5;

    @BeforeEach void setUp() {

        transaction = new Transaction(NamedConstants.ID, NamedConstants.ID, BUY, NETFLIX,
                NamedConstants.FIVE_HUNDRED, TEN, originalTime);
    }

    @Test void testGetId() {

        assertEquals(NamedConstants.ID, transaction.getId());
    }

    @Test void testGetUserId() {

        assertEquals(NamedConstants.ID, transaction.getUserId());
    }

    @Test void testSetUserId() {

        transaction.setUserId(NamedConstants.SECOND_ID);
        assertEquals(NamedConstants.SECOND_ID, transaction.getUserId());
    }

    @Test void testGetType() {

        assertEquals(BUY, transaction.getType());
    }

    @Test void testSetType() {

        transaction.setType(SELL);
        assertEquals(SELL, transaction.getType());
    }

    @Test void testGetStock() {

        assertEquals(NETFLIX, transaction.getStock());
    }

    @Test void testSetStock() {

        transaction.setStock(APPLE);
        assertEquals(APPLE, transaction.getStock());
    }

    @Test void testGetPrice() {

        assertEquals(NamedConstants.FIVE_HUNDRED, transaction.getPrice(), 0.05);
    }

    @Test void testSetPrice() {

        transaction.setPrice(NamedConstants.THREE_HUNDRED);
        assertEquals(NamedConstants.THREE_HUNDRED, transaction.getPrice(), 0.05);
    }

    @Test void testGetShares() {

        assertEquals(TEN, transaction.getShares());
    }

    @Test void testSetShares() {

        transaction.setShares(FIVE);
        assertEquals(FIVE, transaction.getShares());
    }

    @Test void testGetTime() {

        assertEquals(originalTime, transaction.getTime());
    }

    @Test void testSetTime() {

        Date newTime = new Date();
        transaction.setTime(newTime);
        assertEquals(newTime, transaction.getTime());
    }

    @Test void testCreateIncompleteEntry() {

        Transaction incompleteTransaction =
                Transaction.createIncompleteTransaction(NamedConstants.SECOND_ID, SELL,
                        APPLE, NamedConstants.THREE_HUNDRED, FIVE);

        assertNull(incompleteTransaction.getId());
        assertEquals(NamedConstants.SECOND_ID, incompleteTransaction.getUserId());
        assertEquals(SELL, incompleteTransaction.getType());
        assertEquals(APPLE, incompleteTransaction.getStock());
        assertEquals(NamedConstants.THREE_HUNDRED, incompleteTransaction.getPrice(), 0.05);
        assertEquals(FIVE, incompleteTransaction.getShares());
        assertNull(incompleteTransaction.getTime());
    }
}
