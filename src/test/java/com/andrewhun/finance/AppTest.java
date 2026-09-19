package com.andrewhun.finance;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;
import com.andrewhun.finance.database.DatabaseConfig;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.usermapper.UserMapperFactory;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxToolkit;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

public class AppTest {

    @TempDir
    Path tempDir;

    private Stage stage;

    @BeforeAll
    static void setUpHeadlessMode() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @BeforeEach
    void setUp() throws Exception {
        stage = FxToolkit.registerPrimaryStage();
    }

    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.cleanupStages();
        UserMapperFactory.reset();
        AppContext.reset();
    }

    @Test
    @DisplayName("Should show login page when no user is logged in")
    void testDisplayingLoginPage() throws TimeoutException {
        launchApp();
        Assertions.assertEquals("Welcome to Desktop Finance!", stage.getTitle());
    }

    @Test
    @DisplayName("Should show main window when a user is already logged in")
    void testDisplayingMainWindow() throws Exception {
        loginAsNewUser();
        launchApp();
        Assertions.assertEquals("Desktop Finance", stage.getTitle());
    }

    private void launchApp() throws TimeoutException {
        AppContext.reset();
        DatabaseConfig.setTestDirectory(tempDir);
        FxToolkit.setupApplication(() -> new App(StorageMode.IN_MEMORY));
    }

    private void loginAsNewUser() throws Exception {

        AppContext.setStorageMode(StorageMode.IN_MEMORY);
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        user.login();
        UserMapperFactory.create().save(user);
        AppContext.reset();
    }
}