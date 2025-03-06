package com.vasconcellos.voxybases.util;

import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    public static String format(long time) {
        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - days * 24L;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60L;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toMinutes(time) * 60L;

        StringBuilder sb = new StringBuilder();

        if (days > 0L) sb.append(days).append("d");

        if (hours > 0L) {
            if (days > 0)
                sb.append(" ");

            sb.append(hours).append("h");
        }

        if (minutes > 0L) {
            if(hours > 0L)
                sb.append(" ");

            sb.append(minutes).append("m");
        }

        if (seconds > 0L) {
            if(minutes > 0L)
                sb.append(" ");

            sb.append(seconds).append("s");
        }

        return sb.toString();
    }
}