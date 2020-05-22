package com.hanu.ims.util.date;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class TimestampConverter {
    public static Timestamp getTimestampFromSeconds(long seconds) {
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(seconds);
        return new Timestamp(timeSeconds * 1000);
    }
}
