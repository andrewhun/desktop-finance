/*
This file contains the TransactionsTableController class, which is responsible for handling interactions
regarding the Transactions table on the History tab.
 */

package com.andrewhun.finance.mainwindow;

import java.util.List;
import javafx.fxml.FXML;
import java.util.ArrayList;
import javafx.scene.control.*;
import com.andrewhun.finance.models.Transaction;
import com.andrewhun.finance.services.SimpleEntriesTableService;
import com.andrewhun.finance.databaseprocedures.TransactionTableProcedures;

public class TransactionsTableController implements EntriesTableController<Transaction> {

    @FXML TableView<Transaction> transactionsTable;
    @FXML Label transactionsLabel;
    @FXML Button hideTransactions;
    @FXML Button showTransactions;

    @FXML TableColumn<Transaction, String> transactionIdCol;
    @FXML TableColumn<Transaction, String> transactionTypeCol;
    @FXML TableColumn<Transaction, String> transactionStockCol;
    @FXML TableColumn<Transaction, String> transactionPriceCol;
    @FXML TableColumn<Transaction, String> transactionSharesCol;
    @FXML TableColumn<Transaction, String> transactionTimeCol;

    @FXML private void initialize() throws Exception {

        SimpleEntriesTableService<Transaction> service =
                new SimpleEntriesTableService<>(this, new TransactionTableProcedures());
        service.setUpEntriesTable();
    }

    public List<TableColumn<Transaction, String>> getListOfTableColumns() {

        List<TableColumn<Transaction, String>> tableColumns = new ArrayList<>();
        tableColumns.add(transactionIdCol);
        tableColumns.add(transactionTypeCol);
        tableColumns.add(transactionStockCol);
        tableColumns.add(transactionPriceCol);
        tableColumns.add(transactionSharesCol);
        tableColumns.add(transactionTimeCol);
        return tableColumns;
    }

    public List<String> getListOfFieldNames() {

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("id");
        fieldNames.add("type");
        fieldNames.add("stock");
        fieldNames.add("price");
        fieldNames.add("shares");
        fieldNames.add("time");
        return fieldNames;
    }

    public TableView<Transaction> getTable() {

        return transactionsTable;
    }

    public void hideTransactionsTable() {

        showTransactions.setVisible(true);
        hideTransactions.setVisible(false);
        transactionsLabel.setVisible(false);
        transactionsTable.setVisible(false);
    }

    public void showTransactionsTable() {

        showTransactions.setVisible(false);
        hideTransactions.setVisible(true);
        transactionsLabel.setVisible(true);
        transactionsTable.setVisible(true);
    }
}
