/*
* This file contains the TestFXBaseClass, which serves as the base for all test classes
* that rely on TestFx to carry out their tests.
 */

package com.andrewhun.finance;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TableView;
import javafx.scene.AccessibleAttribute;
import com.andrewhun.finance.models.User;
import javafx.geometry.VerticalDirection;
import com.andrewhun.finance.services.TestService;
import org.testfx.framework.junit5.ApplicationTest;

public class TestFxBaseClass extends ApplicationTest {

    protected static User testUser;
    protected Stage stage;


    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        new App().start(stage);
    }

    @BeforeAll
    public static void setUpTestDatabase() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
    }

    @AfterEach
    protected void tearDown() throws Exception {

        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    protected Node getNthRowInTableView(String tableViewId, int rowIndex) {

        TableView tableView = lookup(tableViewId).query();
        return (Node)(tableView.queryAccessibleAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, rowIndex, 0));
    }

    protected void scrollDown() {

        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
    }

    protected void scrollUp() {

        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
        scroll(VerticalDirection.UP);
    }
}