package com.andrewhun.finance.forminputprocessor;

import com.andrewhun.finance.util.NamedConstants;

public class TestEditFormController<T> implements EditFormController<T> {

    private T originalEntry;

    @Override
    public void injectEntry(T entry) {

        this.originalEntry = entry;
    }

    @Override
    public Boolean aChangeWasMade() {return false;}

    @Override
    public Boolean typeWasChanged() {return false;}

    @Override
    public void editType(T entry) {}

    @Override
    public Boolean titleWasChanged() {return false;}

    @Override
    public void editTitle(T entry) {}

    @Override
    public Boolean amountIsValid() {return false;}

    @Override
    public Boolean amountWasChanged() {return false;}

    @Override
    public void editAmount(T entry) {}

    @Override
    public T copyOriginalEntry() {return null;}

    @Override
    public void makeChangesToDatabase(T editedEntry) {}

    @Override
    public void showErrorMessage(String message){}
}
