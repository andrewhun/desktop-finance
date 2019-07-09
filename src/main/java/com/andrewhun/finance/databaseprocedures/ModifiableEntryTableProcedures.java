/*
This file contains the ModifiableEntryTableProcedures interface, which is implemented by
stored procedure classes which are connected to TableViews and allow users to edit or delete entries.
 */

package com.andrewhun.finance.databaseprocedures;

public interface ModifiableEntryTableProcedures<T> extends EntryTableProcedures<T> {

    void deleteEntry(T entry) throws Exception;
    void deleteAllEntriesForUser(Integer userId) throws Exception;
}
