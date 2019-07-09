/*
* This file contains the UserUtil class, which performs auxiliary tasks
* related to User objects. These tasks are performed frequently, which in
* my opinion warrants the creation of a utility class for them.
 */

package com.andrewhun.finance.util;

import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

public class UserUtil {

    private UserUtil() {}

    public static Integer getCurrentUserId() throws Exception {

        return new UserTableProcedures().findLoggedInUser().getId();
    }

    public static Double getCurrentUserBalance() throws Exception {

        return new UserTableProcedures().findLoggedInUser().getBalance();
    }
}
