package com.andrewhun.finance;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxToolkit;

public class AppTest extends ApplicationTest {

    protected Stage stage;


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
        new App().start(stage);
    }

    @Test
    void testDisplayingHelloWorldPage() {
        Assertions.assertEquals("Hello World!", stage.getTitle());
    }
}