/*
* This file contains the RecurringEntryTableProcedures class,
* which is a collection of stored procedures that operate on
* the Recurring entries table of the database.
 */

package com.andrewhun.finance.databaseprocedures;

import com.andrewhun.finance.models.RecurringEntry;

import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RecurringEntryTableProcedures extends ResultSetProcessor<RecurringEntry> {

    public void createRecurringEntriesTable() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS Recurring_entries");

        String queryScript = "CREATE TABLE Recurring_entries (\n"
                + "id INTEGER PRIMARY KEY ASC AUTOINCREMENT\n"
                + "UNIQUE\n"
                + "NOT NULL\n"
                + "DEFAULT(1),\n"
                + "type STRING NOT NULL,\n"
                + "title STRING NOT NULL,\n"
                + "amount DECIMAL NOT NULL,\n"
                + "user_id INTEGER REFERENCES Users (id) ON DELETE CASCADE\n"
                + "NOT NULL,\n"
                + "time DATETIME NOT NULL\n"
                + "CONSTRAINT [DEFAULT CURRENT_TIMESTAMP] DEFAULT\n"
                + "(CURRENT_TIMESTAMP),\n"
                + "frequency STRING NOT NULL\n"
                + ");";
        statement.executeUpdate(queryScript);
        disconnectFromDatabase();
    }

    public void addRecurringEntryToDatabase(RecurringEntry recurringEntry) throws SQLException {

        connectToDatabase();
        String queryScript = "INSERT INTO Recurring_entries(type, title, amount, user_id, frequency) " +
                "VALUES(?, ?, ?, ?, ?)";

        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, recurringEntry.getType());
        preparedStatement.setString(2, recurringEntry.getTitle());
        preparedStatement.setDouble(3, recurringEntry.getAmount());
        preparedStatement.setInt(4, recurringEntry.getUserId());
        preparedStatement.setString(5, recurringEntry.getFrequency());
        preparedStatement.executeUpdate();

        disconnectFromDatabase();
    }

    public RecurringEntry getEntryById(Integer id) throws Exception {

        connectToDatabase();
        String queryScript = "SELECT * FROM Recurring_entries WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        RecurringEntry selectedEntry = getSingleEntry(resultSet);
        disconnectFromDatabase();
        return selectedEntry;
    }

    public List<RecurringEntry> getRecurringEntriesForUser(Integer userId)
            throws Exception {

        connectToDatabase();
        String queryScript = "SELECT * FROM Recurring_entries WHERE user_id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, userId);
        resultSet = preparedStatement.executeQuery();

        List<RecurringEntry> recurringEntries = getListOfEntries(resultSet);
        disconnectFromDatabase();
        return recurringEntries;
    }

    @Override
    RecurringEntry createModelFromResultSet(ResultSet resultSet) throws Exception {

        Integer id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String title = resultSet.getString("title");
        Double amount = resultSet.getDouble("amount");
        Integer userId = resultSet.getInt("user_id");
        String timeString = resultSet.getString("time");
        Date time = parseDateFromString(timeString);
        String frequency = resultSet.getString("frequency");

        return new RecurringEntry(id, type, title, amount, userId, time, frequency);
    }

    private Date parseDateFromString(String timeString) throws ParseException {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeString);
    }
}