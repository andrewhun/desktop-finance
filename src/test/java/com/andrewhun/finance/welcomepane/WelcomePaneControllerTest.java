package com.andrewhun.finance.welcomepane;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.TestFxBaseClass;
import com.andrewhun.finance.services.TestService;
import static com.andrewhun.finance.util.NamedConstants.USERNAME;
import static com.andrewhun.finance.util.GuiElementIds.REGISTER_BUTTON_ID;

class WelcomePaneControllerTest extends TestFxBaseClass {

    @BeforeAll
    public static void setUpTestDatabase() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        populateUsersTable();
    }

    private static void populateUsersTable() throws Exception {

        for (int i = 1; i < 5; i++) {

            TestService.addTestUserToDatabase(USERNAME + i);
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