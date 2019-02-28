package com.andrewhun.finance.databaseprocedures;

import java.sql.SQLException;
import java.sql.DriverManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.andrewhun.finance.util.NamedConstants.INCORRECT_INPUT;

class StoredProceduresBaseClassTest {

    private StoredProceduresBaseClass storedProceduresBaseClass = new StoredProceduresBaseClass();

    @Test
    void testConnectingToProductionDatabase() throws SQLException {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.PRODUCTION;
        storedProceduresBaseClass.connectToDatabase();
        String url = storedProceduresBaseClass.connection.getMetaData().getURL();
        Assertions.assertEquals(storedProceduresBaseClass.productionDatabaseUrl, url);
        storedProceduresBaseClass.disconnectFromDatabase();
    }

    @Test
    void testConnectingToTestDatabase() throws SQLException {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
        storedProceduresBaseClass.connectToDatabase();
        String url = storedProceduresBaseClass.connection.getMetaData().getURL();
        Assertions.assertEquals(storedProceduresBaseClass.testDatabaseUrl, url);
        storedProceduresBaseClass.disconnectFromDatabase();
    }

    @Test
    void testConnectingToBogusURL() {

        assertThrows(SQLException.class,
                () -> storedProceduresBaseClass.connection = DriverManager.getConnection(INCORRECT_INPUT));
    }
}
