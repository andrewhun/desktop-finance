/*
This file contains the PageChanger interface, which is implemented by classes that control components of the
user interface. These classes react to certain user actions by changing the current page to a different one.
 */

package com.andrewhun.finance.uiaction;

import com.andrewhun.finance.util.NamedConstants;

public interface PageChanger {

    default String getNewPageTitle() {

        return NamedConstants.MAIN_WINDOW_TITLE;
    }

    default String getNewPagePath() {

        return NamedConstants.MAIN_WINDOW_PATH;
    }
}
