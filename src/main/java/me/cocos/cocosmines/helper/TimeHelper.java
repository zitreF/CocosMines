package me.cocos.cocosmines.helper;

import java.text.SimpleDateFormat;

public final class TimeHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private TimeHelper() {}

    public static String format(long millis) {
        return DATE_FORMAT.format(millis);
    }

    public static String convertMillisecondsToTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append("h ");
        }

        if (minutes > 0 || hours > 0) {
            result.append(minutes).append("min ");
        }

        result.append(seconds).append("s");

        return result.toString();
    }
}
