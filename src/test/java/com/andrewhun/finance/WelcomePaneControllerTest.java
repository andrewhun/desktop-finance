/*
* This file contains the WelcomePaneControllerTest class, which is responsible
* for testing the functionality of the welcome pane. This currently means
* testing that the welcome pane limits the number of users by disabling
* the create profile (alias register) tab.
 */

package com.andrewhun.finance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;

class WelcomePaneControllerTest extends TestFxBaseClass {

    @BeforeAll
    static void setUpTestDatabase() throws Exception {

        TestFxBaseClass.setUpTestDatabase();
        populateUsersTable();
    }

    private static void populateUsersTable() throws Exception {

        for (int i = 1; i < 5; i++) {

            testProcedures.addTestUserToDatabase(USERNAME + i);
        }
    }

    @Test
    void testLimitingTheNumberOfUsers() {

        /* I'm using the register button because the check for the tab
        returns false, even though the tab is blatantly and visibly disabled.
         If you would like to confirm this, add a sleep to the test and see for yourself.*/
        Assertions.assertTrue(lookup(REGISTER_BUTTON_ID).query().isDisabled());
    }
}
