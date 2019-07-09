/*
 * This class is used for testing the ModifiableEntriesTableService class. Using an inner class caused
 * compiler errors and undesirable behavior, thus this class was created.
 */
package com.andrewhun.finance.models;

public class TestEntry implements ModifiableEntry {

    private String test;

    public TestEntry(String test) {

        this.test = test;
    }

    public String getTest() {

        return test;
    }

    public void setTest(String test) {

        this.test = test;
    }
}
