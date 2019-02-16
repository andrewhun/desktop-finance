/* This file contains the StoredProceduresBaseClass class, which serves as the base for all
 * classes in this package. It provides the variables and methods that are used for connecting to
 * the database and disconnecting from it. The implementation relies on SQLite3 and the JDBC API.
 */

package com.andrewhun.finance.databaseprocedures;

import java.sql.*;

public class StoredProceduresBaseClass {

    private final String DATABASE_PATH = System.getProperty("user.dir") + "/src/main/resources/database/";
    private final String URL_STUB = "jdbc:sqlite:" + DATABASE_PATH;
    private String currentDatabaseFile = "desktop_finance_2019.db";
    private String productionDatabaseUrl = URL_STUB + currentDatabaseFile;
    private String testDatabaseUrl = URL_STUB + "test_database.db";

    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    /* Making this variable static allows developers to switch to the test database on the scope
    of the whole application, using a single command*/
    public static ActiveDatabase activeDatabase = ActiveDatabase.PRODUCTION;

    void connectToDatabase() {
        try {
            switch (activeDatabase) {
                case PRODUCTION:
                    connection = DriverManager.getConnection(productionDatabaseUrl);
                    break;

                case TEST:
                    connection = DriverManager.getConnection(testDatabaseUrl);
            }
        }
        catch (SQLException e) {

            e.printStackTrace();
        }
    }

    void disconnectFromDatabase() {

        closeResultSet();
        closePreparedStatement();
        closeStatement();
        closeConnection();
    }

    private void closeResultSet() {

        if (resultSet != null) {

            try {
                resultSet.close();
            }
            catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    private void closePreparedStatement() {

        if (preparedStatement != null) {

            try {

                preparedStatement.close();
            }
            catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    private void closeStatement() {

        if (statement != null) {

            try {

                statement.close();
            }
            catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    private void closeConnection() {

        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
}
