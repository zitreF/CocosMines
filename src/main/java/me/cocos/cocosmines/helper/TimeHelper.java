package me.cocos.cocosmines.helper;

import java.text.SimpleDateFormat;

public final class TimeHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private TimeHelper() {}

    public static String format(long millis) {
        return DATE_FORMAT.format(millis);
    }
}
