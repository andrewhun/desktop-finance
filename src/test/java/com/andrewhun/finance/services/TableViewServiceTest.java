package com.andrewhun.finance.services;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.Assertions;
import javafx.scene.control.TableColumn;
import org.testfx.framework.junit5.ApplicationTest;

class TableViewServiceTest extends ApplicationTest {

    private TableViewService<Object> tableViewService = new TableViewService<>();

    @Test void testPopulateTableView() {

        TableView<Object> tableView = new TableView<>();
        List<Object> objectList = new ArrayList<>();
        objectList.add(new Object());
        objectList.add(new Object());
        tableViewService.populateTableView(tableView, objectList);
        System.out.println(objectList);
        Assertions.assertEquals(objectList, tableView.getItems());
    }

    @Test void testMapColumnsToFieldsWithMatchingLists() {

        List<TableColumn<Object, String>> tableColumns = createListOfTableColumns(2);

        tableViewService.mapColumnsToFields(tableColumns, createListOfFieldNames());

        for (TableColumn tableColumn : tableColumns) {
            Assertions.assertNotNull(tableColumn.getCellValueFactory());
        }
    }

    @Test void testMapColumnsToFieldsWithMismatch() {

        List<TableColumn<Object, String>> tableColumns = createListOfTableColumns(3);

        tableViewService.mapColumnsToFields(tableColumns, createListOfFieldNames());
        for (TableColumn tableColumn : tableColumns) {

            Assertions.assertNull(tableColumn.getCellValueFactory());
        }
    }

    private List<TableColumn<Object, String>> createListOfTableColumns(int length) {

        List<TableColumn<Object, String>> tableColumns = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            tableColumns.add(new TableColumn<>());
        }

        return tableColumns;
    }

    private List<String> createListOfFieldNames() {

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("id");
        fieldNames.add("test");

        return fieldNames;
    }
}
