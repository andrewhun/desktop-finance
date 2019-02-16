module desktopfinance {

    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.controls;
    requires javafx.base;
    requires java.sql;

    opens com.andrewhun.finance;
    opens com.andrewhun.finance.databaseprocedures;
    opens com.andrewhun.finance.welcomepane;
    opens com.andrewhun.finance.mainwindow;
}