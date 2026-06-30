package com.andrewhun.finance.registerpage;

import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterPageController {

    @FXML private Hyperlink backToLoginLink;

    @FXML
    void handleBackToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) backToLoginLink.getScene().getWindow();
            WindowNavigator.showWindow(stage, Page.LOGIN);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}