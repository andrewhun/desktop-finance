/*
This file contains the PageReloader interface, which is implemented by classes that control components
of the user interface. These classes react to certain user actions by reloading/refreshing the current page.
 */

package com.andrewhun.finance.uiaction;

import java.net.URL;

public interface PageReloader {

    URL getLocation();
}
