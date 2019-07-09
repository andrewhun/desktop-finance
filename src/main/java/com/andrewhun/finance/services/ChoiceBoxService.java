/*
* This file contains the ChoiceBoxService class, which is responsible for
* populating ChoiceBox objects according to the instructions it receives.
 */

package com.andrewhun.finance.services;

import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChoiceBoxService {

    final static String INCOME = "Income";
    final static String EXPENSE = "Expense";
    final static String DAILY = "Daily";
    final static String WEEKLY = "Weekly";
    final static String MONTHLY = "Monthly";

    private ObservableList<String> choices;

    public void fillOutChoiceBox(ChoiceBox<String> choiceBox, EntryAttribute attribute) {

        switch (attribute) {

            case TYPE:
                choices = FXCollections.observableArrayList("", INCOME, EXPENSE);
                break;

            case FREQUENCY:
                choices = FXCollections.observableArrayList("", DAILY, WEEKLY, MONTHLY);
                break;
        }

        choiceBox.setItems(choices);
    }
}
