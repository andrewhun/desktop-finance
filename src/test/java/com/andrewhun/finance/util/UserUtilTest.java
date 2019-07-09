package com.andrewhun.finance.util;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.services.TestService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.andrewhun.finance.exceptions.EntryNotFoundException;

class UserUtilTest {

    private User testUser;

    @BeforeEach
    void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
    }

    @Test void testGetCurrentUserId() throws Exception {

        testUser.login();
        Assertions.assertEquals(testUser.getId(), UserUtil.getCurrentUserId());
    }

    @Test void tetGetCurrentUserBalance() throws Exception {

        testUser.login();
        Assertions.assertEquals(testUser.getBalance(), UserUtil.getCurrentUserBalance());
    }

    @Test void testEntryNotFoundExceptionIsThrown() {

        assertThrows(EntryNotFoundException.class, UserUtil :: getCurrentUserId);
        assertThrows(EntryNotFoundException.class, UserUtil :: getCurrentUserBalance);
    }
}
