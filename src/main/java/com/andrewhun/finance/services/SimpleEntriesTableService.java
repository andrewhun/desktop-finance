/*
This file contains the SimpleEntriesTableService class, which encapsulate the basic logic of handling
TableViews (setting up and populating them).
 */

package com.andrewhun.finance.services;

import com.andrewhun.finance.models.User;
import com.andrewhun.finance.mainwindow.EntriesTableController;
import com.andrewhun.finance.databaseprocedures.EntryTableProcedures;

public class SimpleEntriesTableService<T> implements EntriesTableService {

    private EntriesTableController<T> controller;
    private EntryTableProcedures<T> tableProcedures;
    private TableViewService<T> tableViewService = new TableViewService<>();

    public SimpleEntriesTableService
            (EntriesTableController<T> controller, EntryTableProcedures<T> tableProcedures) {

        this.controller = controller;
        this.tableProcedures = tableProcedures;
    }

    public void setUpEntriesTable() throws Exception {

        setUpTableColumns();
        populateTable();
    }

    private void setUpTableColumns() {

        tableViewService.mapColumnsToFields(controller.getListOfTableColumns(), controller.getListOfFieldNames());
    }

    private void populateTable() throws Exception {

        tableViewService.populateTableView(controller.getTable(),
                tableProcedures.findEntriesForUser(User.getCurrentUserId()));
    }
}