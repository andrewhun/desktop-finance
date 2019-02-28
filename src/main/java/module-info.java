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
    opens com.andrewhun.finance.models;
    opens com.andrewhun.finance.exceptions;
    opens com.andrewhun.finance.security;
    opens com.andrewhun.finance.services;
    opens com.andrewhun.finance.util;
}
