/*
This file contains the EntryTableProcedures interface, which is implemented by stored procedure
classes that are connected to TableViews but do NOT allow users to edit or delete entries.
 */

package com.andrewhun.finance.databaseprocedures;

import java.util.List;

public interface EntryTableProcedures<T> {

    List<T> findEntriesForUser(Integer userId) throws Exception;
}
