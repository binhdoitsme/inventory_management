package com.hanu.ims.util.date;

import java.time.Instant;

public class EpochSecondConverter {
    public static String epochSecondToString(long timestamp) {
        return Instant.ofEpochSecond(timestamp).toString()
                .replace("T", " ").replace("Z", "");
    }
}
