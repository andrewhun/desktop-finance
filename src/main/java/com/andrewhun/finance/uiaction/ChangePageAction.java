/*
This file contains the ChangePageAction class, which is responsible for changing the current page to the
specified one when it is appropriate.
 */

package com.andrewhun.finance.uiaction;

import javafx.stage.Stage;
import com.andrewhun.finance.services.WindowService;

public class ChangePageAction implements UIAction {

    private PageChanger changer;

    public ChangePageAction(PageChanger changer) {

        this.changer = changer;
    }

    public void performAction(Stage stage) {

        new WindowService(stage).showSelectedWindow(changer.getNewPagePath(), changer.getNewPageTitle());
    }
}
