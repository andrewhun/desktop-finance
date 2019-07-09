/*
* This file contains the StatementEntryTableProcedures class,
* which is a collection of stored procedures that operate on
* the Statement entries table of the database.
 */

package com.andrewhun.finance.databaseprocedures;

import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.andrewhun.finance.util.DateUtil;
import com.andrewhun.finance.models.StatementEntry;

public class StatementEntryTableProcedures extends ResultSetProcessor<StatementEntry>
implements ModifiableEntryTableProcedures<StatementEntry> {

    public void createTable() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS Statement_entries");

        String queryScript = "CREATE TABLE Statement_entries (\n "
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
                + "(CURRENT_TIMESTAMP)\n"
                + ");";
        statement.executeUpdate(queryScript);
        disconnectFromDatabase();
    }

    public void addEntryToDatabase(StatementEntry incompleteEntry)
        throws SQLException {

        connectToDatabase();
        String queryScript = "INSERT INTO Statement_entries (type, title, amount, user_id)"
                + "VALUES (?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, incompleteEntry.getType());
        preparedStatement.setString(2, incompleteEntry.getTitle());
        preparedStatement.setDouble(3, incompleteEntry.getAmount());
        preparedStatement.setInt(4, incompleteEntry.getUserId());
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    public StatementEntry findById(Integer id) throws Exception {

        connectToDatabase();
        String queryScript = "SELECT * FROM Statement_entries WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        StatementEntry selectedEntry = getSingleEntry(resultSet);
        disconnectFromDatabase();

        return selectedEntry;
    }

    public List<StatementEntry> findEntriesForUser(Integer userId) throws Exception {

        connectToDatabase();
        String queryScript = "SELECT * FROM Statement_entries WHERE user_id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, userId);
        resultSet = preparedStatement.executeQuery();
        List<StatementEntry> statementEntries = getListOfEntries(resultSet);
        disconnectFromDatabase();

        return statementEntries;
    }

    public void editEntry(StatementEntry entry) throws Exception {

        connectToDatabase();
        String queryScript = "UPDATE Statement_entries SET type = ?, title = ?, amount = ? WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, entry.getType());
        preparedStatement.setString(2, entry.getTitle());
        preparedStatement.setDouble(3, entry.getAmount());
        preparedStatement.setInt(4, entry.getId());
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    public void deleteEntry(StatementEntry entry) throws Exception {

        connectToDatabase();
        String queryScript = "DELETE FROM Statement_entries WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, entry.getId());
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    public void deleteAllEntriesForUser(Integer userId) throws Exception {

        connectToDatabase();
        String queryScript = "DELETE FROM Statement_entries WHERE user_id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    @Override
    StatementEntry createModelFromResultSet(ResultSet resultSet) throws Exception {

        Integer id = resultSet.getInt("id");
        String type = resultSet.getString("type");
        String title = resultSet.getString("title");
        Double amount = resultSet.getDouble("amount");
        Integer userId = resultSet.getInt("user_id");
        String timeString = resultSet.getString("time");
        Date time = DateUtil.parseDateFromString(timeString);

        return new StatementEntry(id, type, title, amount, userId, time);
    }
}
