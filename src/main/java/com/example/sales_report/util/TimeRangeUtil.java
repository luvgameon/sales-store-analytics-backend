package com.example.sales_report.util;

import java.time.LocalDate;
import java.time.Month;

public class TimeRangeUtil {

    public static LocalDate[] resolveRange(
            LocalDate start,
            LocalDate end,
            String timeRange
    ) {

        if (timeRange.equalsIgnoreCase("range")) {
            // special flag: skip date filtering
            return new LocalDate[]{start, end};
        }
        if( start == null ) {
            start = LocalDate.now();
        }

        return switch (timeRange.toLowerCase()) {

            case "month" -> new LocalDate[]{
                    start.withDayOfMonth(1),
                    start.withDayOfMonth(start.lengthOfMonth())
            };

            case "quarter" -> {
                int quarter = (start.getMonthValue() - 1) / 3;
                Month firstMonth = Month.of(quarter * 3 + 1);
                LocalDate qStart = LocalDate.of(start.getYear(), firstMonth, 1);
                LocalDate qEnd = qStart.plusMonths(3).minusDays(1);
                yield new LocalDate[]{qStart, qEnd};
            }

            case "year" -> new LocalDate[]{
                    LocalDate.of(start.getYear(), 1, 1),
                    LocalDate.of(start.getYear(), 12, 31)
            };

            default -> throw new IllegalArgumentException(
                    "Invalid timeRange: " + timeRange
            );
        };
    }
}
