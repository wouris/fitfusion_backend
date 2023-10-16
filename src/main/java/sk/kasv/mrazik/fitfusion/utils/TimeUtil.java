package sk.kasv.mrazik.fitfusion.utils;

import java.sql.Timestamp;

public class TimeUtil {
    public static String getTimeAgo(Timestamp createdAt) {
        long now = System.currentTimeMillis();
        long createdAtMillis = createdAt.getTime();
        long diff = now - createdAtMillis;

        long seconds = diff / 1000;
        if (seconds < 60) {
            return seconds + " seconds ago";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes ago";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hours ago";
        }

        long days = hours / 24;
        return days + " days ago";
    }
}
