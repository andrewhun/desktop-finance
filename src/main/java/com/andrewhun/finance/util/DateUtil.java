/*
* This file contains the DateUtil class, which is responsible for parsing
* dates in a format that is usable both in the database and in the rest of
* the application.
 */

package com.andrewhun.finance.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static Date parseDateFromString(String time) throws ParseException {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
    }
}
