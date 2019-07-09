/*
This file contains the ModifiableEntriesTableController interface, which is implemented by classes that
control TableViews which allow users to edit and delete existing database records.
 */

package com.andrewhun.finance.mainwindow;

import java.net.URL;
import javafx.scene.control.*;

public interface ModifiableEntriesTableController<T> extends EntriesTableController<T> {


    String getEditFormPath();
    Label getEntriesTableError();
    URL getLocation();

    default Boolean entryCanBeEdited(T entry) {

        return (entry != null);
    }

    default Boolean entryCanBeDeleted(T entry) {

        // By default, all entries can be modified and deleted.
        return entryCanBeEdited(entry);
    }

    default void carryOutTasksAfterDeletingSelectedEntry(T selectedEntry) throws Exception {

        // By default, no actions are taken after deleting a selected entry of a certain type.
    }
    default Boolean userHasConfirmedDeletingAllEntries() {

        // By default, no confirmation process is in place for deleting all entries of a certain type.
        return true;
    }

    default void carryOutTasksAfterDeletingAllEntries() throws Exception {
        // By default, no actions will be taken after deleting all entries of a certain type.
    }


}
