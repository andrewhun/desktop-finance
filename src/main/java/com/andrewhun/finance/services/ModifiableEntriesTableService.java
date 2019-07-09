/*
This file contains the ModifiableEntriesTableService class, which encapsulates the logic of handling
TableViews that allow users to edit and delete existing database records.
 */

package com.andrewhun.finance.services;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import com.andrewhun.finance.util.*;
import com.andrewhun.finance.models.ModifiableEntry;
import com.andrewhun.finance.forminputprocessor.EditFormController;
import com.andrewhun.finance.mainwindow.ModifiableEntriesTableController;
import com.andrewhun.finance.databaseprocedures.ModifiableEntryTableProcedures;
import static com.andrewhun.finance.util.ErrorMessages.NO_ENTRY_SELECTED_MESSAGE;

public class ModifiableEntriesTableService<T extends ModifiableEntry>
       implements EntriesTableService {

    private ModifiableEntriesTableController<T> controller;
    private ModifiableEntryTableProcedures<T> tableProcedures;

    public ModifiableEntriesTableService
            (ModifiableEntriesTableController<T> controller,
                                         ModifiableEntryTableProcedures<T> tableProcedures) {

        this.controller = controller;
        this.tableProcedures = tableProcedures;
    }

    public void setUpEntriesTable() throws Exception {

        SimpleEntriesTableService<T> service =
                new SimpleEntriesTableService<>(controller, tableProcedures);
        service.setUpEntriesTable();
    }

    public void handleModificationRequest(TableAction action) throws Exception {

        T selectedEntry = controller.getTable().getSelectionModel().getSelectedItem();
        switch (action) {

            case EDIT:
                if (controller.entryCanBeEdited(selectedEntry)) {
                    showEditForm(selectedEntry);
                    return;
                }

            case DELETE:
                if(controller.entryCanBeDeleted(selectedEntry)) {
                    deleteSelectedEntry(selectedEntry);
                    return;
                }

        }
        /* If the method reaches this point, then either no entry was selected or the
        selected entry does not fit the criteria for the selected action. */
        controller.getEntriesTableError().setText(NO_ENTRY_SELECTED_MESSAGE);
    }

    private void showEditForm(T selectedEntry) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.getEditFormPath()));
        Parent root = loader.load();

        try{
            passEntryToEditFormController(loader, selectedEntry);
        }
        catch (Exception classIsNotAnEditFormController) {

            classIsNotAnEditFormController.printStackTrace();
            return;
        }

        WindowService windowService = new WindowService(new Stage());
        windowService.showPopupWindow("Edit selected entry", root);
    }

    private void passEntryToEditFormController(FXMLLoader loader, T selectedEntry) throws Exception {

        try {

            EditFormController<ModifiableEntry> controller;
            controller = loader.getController();
            controller.injectEntry(selectedEntry);
            controller.setUpFormForBalanceEntry(selectedEntry);
        }

        catch (ClassCastException | NullPointerException e) {

            throw new Exception(e.getMessage());
        }
    }

    private void deleteSelectedEntry(T selectedEntry) throws Exception {

        tableProcedures.deleteEntry(selectedEntry);
        controller.carryOutTasksAfterDeletingSelectedEntry(selectedEntry);
        reloadPage();
    }

    public void deleteAllEntriesForCurrentUser() throws Exception {

        if (controller.userHasConfirmedDeletingAllEntries()) {
            tableProcedures.deleteAllEntriesForUser(UserUtil.getCurrentUserId());
            controller.carryOutTasksAfterDeletingAllEntries();
            reloadPage();
        }
    }

    private void reloadPage() throws Exception {

        Stage stage = (Stage) controller.getTable().getScene().getWindow();
        WindowUtil.reloadPage(stage, controller.getLocation());
    }
}