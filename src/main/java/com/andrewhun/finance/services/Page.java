package com.andrewhun.finance.services;

public enum Page {

    LOGIN("/fxml/LoginPage.fxml", "Welcome to Desktop Finance!"),
    REGISTER("/fxml/RegisterPage.fxml", "Create Account"),
    MAIN_WINDOW("/fxml/MainWindow.fxml", "Desktop Finance");

    private final String fxmlPath;
    private final String title;

    Page(String fxmlPath, String title) {
        this.fxmlPath = fxmlPath;
        this.title = title;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public String getTitle() {
        return title;
    }
}