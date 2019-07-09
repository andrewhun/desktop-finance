/*
This file contains the UIActionFactory class, which is responsible for instantiating UIAction objects.
 */

package com.andrewhun.finance.uiaction;

public class UIActionFactory {

    public static UIAction createAction(PageChanger changer) {

        return new ChangePageAction(changer);
    }

    public static UIAction createAction(PageReloader reloader) {

        return new ReloadPageAction(reloader);
    }
}
