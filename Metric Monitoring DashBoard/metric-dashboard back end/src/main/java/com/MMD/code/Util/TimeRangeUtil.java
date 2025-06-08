package com.MMD.code.Util;

import com.MMD.code.Entity.TimeRange;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

    public class TimeRangeUtil {

        public static LocalDateTime getStartTime(TimeRange timeRange) {
            LocalDateTime now = LocalDateTime.now();

            switch (timeRange) {
                case TODAY:
                    return now.truncatedTo(ChronoUnit.DAYS);
                case YESTERDAY:
                    return now.minusDays(1).truncatedTo(ChronoUnit.DAYS);
                case LAST_WEEK:
                    return now.minusWeeks(1).truncatedTo(ChronoUnit.DAYS);
                default:
                    throw new IllegalArgumentException("Unsupported time range: " + timeRange);
            }
        }

        public static LocalDateTime getEndTime(TimeRange timeRange) {
            LocalDateTime now = LocalDateTime.now();

            switch (timeRange) {
                case TODAY:
                    return now;
                case YESTERDAY:
                    return now.truncatedTo(ChronoUnit.DAYS);
                case LAST_WEEK:
                    return now;
                default:
                    throw new IllegalArgumentException("Unsupported time range: " + timeRange);
            }
        }
    }
