/*
This file contains the EditFormController interface, which is implemented by classes that control forms which
allow users to edit existing records in the database.
 */

package com.andrewhun.finance.forminputprocessor;

public interface EditFormController<T> {

    void injectEntry(T entry);
    Boolean aChangeWasMade();
    Boolean typeWasChanged();
    void editType(T entry);
    Boolean titleWasChanged();
    void editTitle(T entry);
    Boolean amountIsValid();
    Boolean amountWasChanged();
    void editAmount(T entry);
    T copyOriginalEntry();
    void makeChangesToDatabase(T editedEntry) throws Exception;
    void showErrorMessage(String message);
    default void editUniqueFields(T editedEntry) {

        /* By default, the interface only handles common fields: type title and amount.
         Any controller classes that are responsible for editing objects with fields other than
          these should overwrite this method accordingly. */
    }
    default void setUpFormForBalanceEntry(T entry) {

        /* This method allows implementors to adjust the form when a balance entry is selected for editing.
         * Since not all forms deal with balance entries, the method is left empty by default.*/
    }
}
