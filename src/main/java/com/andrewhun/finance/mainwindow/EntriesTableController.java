/*
This file contains the EntriesTableController interface, which is implemented by classes that control
TableViews which do NOT allow users to edit or delete existing database records.
 */

package com.andrewhun.finance.mainwindow;

import java.util.List;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public interface EntriesTableController<T> {

    List<TableColumn<T, String>> getListOfTableColumns();
    List<String> getListOfFieldNames();
    TableView<T> getTable();
}
