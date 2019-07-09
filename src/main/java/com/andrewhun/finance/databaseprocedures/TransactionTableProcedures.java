/*
This file contains the TransactionTableProcedures class, which contains a collection of stored
procedures which operate on the Transactions table of the database.
 */

package com.andrewhun.finance.databaseprocedures;

import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.andrewhun.finance.util.DateUtil;
import com.andrewhun.finance.models.Transaction;

public class TransactionTableProcedures extends ResultSetProcessor<Transaction>
        implements EntryTableProcedures<Transaction> {

    public void createTable() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS Transactions");

        String queryScript = "CREATE TABLE Transactions (\n "
                + "id INTEGER PRIMARY KEY ASC AUTOINCREMENT\n"
                + "UNIQUE\n"
                + "NOT NULL\n"
                + "DEFAULT(1),\n"
                + "user_id INTEGER REFERENCES Users (id) ON DELETE CASCADE\n"
                + "NOT NULL,\n"
                + "type STRING NOT NULL,\n"
                + "stock STRING NOT NULL,\n"
                + "price_per_share DECIMAL NOT NULL,\n"
                + "shares INTEGER NOT NULL,\n"
                + "time DATETIME NOT NULL\n"
                + "CONSTRAINT [DEFAULT CURRENT_TIMESTAMP] DEFAULT (CURRENT_TIMESTAMP)\n"
                + ");";
        statement.executeUpdate(queryScript);
        disconnectFromDatabase();
    }

    public void addEntryToDatabase (Transaction incompleteTransaction) throws SQLException {

        connectToDatabase();
        String queryScript = "INSERT INTO Transactions (user_id, type, stock, price_per_share, shares) " +
                "VALUES (?, ?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, incompleteTransaction.getUserId());
        preparedStatement.setString(2, incompleteTransaction.getType());
        preparedStatement.setString(3, incompleteTransaction.getStock());
        preparedStatement.setDouble(4, incompleteTransaction.getPrice());
        preparedStatement.setInt(5, incompleteTransaction.getShares());
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    public Transaction findById(Integer id) throws Exception {

        connectToDatabase();
        String queryScript = "SELECT * FROM Transactions WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        Transaction transaction = getSingleEntry(resultSet);
        disconnectFromDatabase();

        return transaction;
    }

    public List<Transaction> findEntriesForUser(Integer userId) throws Exception {

        connectToDatabase();
        String queryScript = "SELECT * FROM Transactions WHERE user_id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, userId);
        resultSet = preparedStatement.executeQuery();
        List<Transaction> transactions = getListOfEntries(resultSet);
        disconnectFromDatabase();
        return transactions;
    }

    public void deleteAllTransactionsForUser(Integer userId) throws SQLException {

        connectToDatabase();
        String queryScript = "DELETE FROM Transactions WHERE user_id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
        }

    Transaction createModelFromResultSet(ResultSet resultSet) throws Exception {


        Integer id = resultSet.getInt("id");
        Integer userId = (resultSet.getInt("user_id"));
        String type =  resultSet.getString("type");
        String stock = resultSet.getString("stock");
        Double price = resultSet.getDouble("price_per_share");
        Integer shares = resultSet.getInt("shares");
        String timeString = resultSet.getString("time");
        Date time = DateUtil.parseDateFromString(timeString);

        return new Transaction(id, userId, type, stock, price, shares, time);
    }
}
