package com.andrewhun.finance.services;

import java.net.URL;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.junit.jupiter.api.*;
import javafx.event.ActionEvent;
import com.andrewhun.finance.util.*;
import javafx.application.Application;
import com.andrewhun.finance.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.testfx.framework.junit5.ApplicationTest;
import com.andrewhun.finance.databaseprocedures.EntryTableProcedures;
import com.andrewhun.finance.mainwindow.ModifiableEntriesTableController;

public class ModifiableEntriesTableServiceTest extends ApplicationTest {

    private ModifiableEntry entry = createTestEntry("first");
    private TableView<ModifiableEntry> tableView;
    private Button button;
    private EntryTableProcedures<ModifiableEntry> tableProcedures = createTableProcedures();
    private ModifiableEntriesTableController<ModifiableEntry> controller = createController();
    private Application application = createApplication();
    private List<ModifiableEntry> entries;
    private Label entriesTableError;
    private ModifiableEntriesTableService<ModifiableEntry> service;

    @BeforeEach void setUpEntries() throws Exception {

        entries = new ArrayList<>();
        entries.add(entry);
        entries.add(createTestEntry("second"));

        service = createService();
        service.setUpEntriesTable();
    }

    @BeforeAll static void setUpUser() throws Exception {

        User testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();
    }

    @Override public void start(Stage primaryStage) throws Exception {

        entriesTableError = new Label();
        application.start(primaryStage);
    }

    @Test void testSetUpModifiableEntriesTable() {

        // The table is set up before each test. This test is intended to show the default scenario.
        Assertions.assertEquals(2, tableView.getItems().size());
        Assertions.assertEquals(entries, tableView.getItems());
    }

    @Test void testShowEditEntryForm() {

        tableView.getSelectionModel().select(0);
        Stage stage = (Stage) tableView.getScene().getWindow();
        clickOn(GuiElementIds.TEST_BUTTON_ID);

        ObservableList windows = Stage.getWindows();
        Assertions.assertEquals(1, windows.size());
        Assertions.assertTrue(windows.contains(stage));

    }

    @Test void testDeleteSelectedEntry() throws Exception {

        tableView.getSelectionModel().select(0);
        service.handleModificationRequest(TableAction.DELETE);
        Assertions.assertEquals(1, tableView.getItems().size());
    }

    @Test void testShowErrorMessage() throws Exception {

        service.handleModificationRequest(TableAction.DELETE);
        Assertions.assertEquals(ErrorMessages.NO_ENTRY_SELECTED_MESSAGE, entriesTableError.getText());
    }


    ModifiableEntriesTableService<ModifiableEntry> createService() {

        return new ModifiableEntriesTableService<>(controller, tableProcedures);
    }

    private EntryTableProcedures<ModifiableEntry> createTableProcedures() {

        return new EntryTableProcedures<>() {

            @Override
            public List<ModifiableEntry> findEntriesForUser(Integer userId){
                return entries;
            }
        };
    }

    private TestEntry createTestEntry(String test) {

        return new TestEntry(test) {
            @Override
            public void delete() {

                entries.remove(this);
                tableView.getItems().setAll(entries);
            }
        };
    }

    private ModifiableEntriesTableController<ModifiableEntry> createController() {

        return  new ModifiableEntriesTableController<>() {

            @Override
            public TableView<ModifiableEntry> getTable() {

                return tableView;

            }

            @Override
            public List<TableColumn<ModifiableEntry, String>> getListOfTableColumns() {

                return (ObservableList) FXCollections.observableArrayList(tableView.getColumns());
            }

            @Override
            public List<String> getListOfFieldNames() {

                List<String> fieldNames = new ArrayList<>();
                fieldNames.add("test");
                return fieldNames;
            }

            @Override
            public String getEditFormPath() {

                return NamedConstants.TEST_EDIT_FORM_PATH;
            }

            @Override
            public Label getEntriesTableError() {
                return entriesTableError;
            }

            @Override
            public URL getLocation() {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(NamedConstants.TEST_WINDOW_PATH));
                return loader.getLocation();
            }
        };
    }

    private Application createApplication() {

        return new Application() {

            @Override
            public void start(Stage primaryStage) throws Exception {

                FXMLLoader loader = new FXMLLoader(controller.getLocation());
                Parent root = loader.load();
                tableView =  (TableView<ModifiableEntry>) root.lookup("#testTable");
                button = (Button) root.lookup(GuiElementIds.TEST_BUTTON_ID);
                setButtonAction(button);
                Scene scene = new Scene(root, 300, 300);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        };
    }

    private void setButtonAction(Button button) {

        button.setOnAction((ActionEvent event) -> {
                try {
                    service.handleModificationRequest(TableAction.EDIT);
                }
                catch (Exception ignore) {

                }
        });
    }
}