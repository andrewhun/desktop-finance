/*
This file contains the ReloadPageAction class, which is responsible for reloading the page which is
currently displayed when it is appropriate.
 */

package com.andrewhun.finance.uiaction;

import javafx.stage.Stage;
import com.andrewhun.finance.util.WindowUtil;

public class ReloadPageAction implements UIAction {

    private PageReloader reloader;

    public ReloadPageAction(PageReloader reloader) {

        this.reloader = reloader;
    }

    public void performAction(Stage stage) throws Exception {

        WindowUtil.reloadPage(stage, reloader.getLocation());
    }
}
