package com.andrewhun.finance;

import com.andrewhun.finance.database.DatabaseExtension;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.user.UserMapper;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationTest;

import java.math.BigDecimal;

@ExtendWith(DatabaseExtension.class)
public class AppWhenUserIsLoggedInTest extends ApplicationTest {

    private Stage stage;

    @BeforeAll
    static void setUpHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        UserMapper userMapper = new UserMapper();
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        userMapper.save(user);
        user.login();
        userMapper.save(user);
        new App().start(stage);
    }

    @Test
    @DisplayName("Should show main window when a user is already logged in")
    void testDirectsToMainWindowWhenUserIsAlreadyLoggedIn() {
        Assertions.assertEquals("Desktop Finance", stage.getTitle());
    }
}