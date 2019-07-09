/*
* This file contains the TableViewService class, which is responsible for
* populating TableViews and their TableColumns.
 */

package com.andrewhun.finance.services;

import java.util.List;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewService <T> {

    public void mapColumnsToFields(List<TableColumn<T, String>> tableColumns, List<String> fieldNames) {

        if (tableColumns.size() == fieldNames.size()) {

            for(int i = 0; i < tableColumns.size(); i++) {

                mapColumnToField(tableColumns.get(i), fieldNames.get(i));
            }
        }
    }

    private void mapColumnToField(TableColumn<T, String> column, String fieldName) {

        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
    }

    public void populateTableView(TableView<T> tableView, List<T> entriesForTable){

        tableView.getItems().setAll(entriesForTable);
    }
}