package com.example.sales_report.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ActiveStoreKpiRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    // month | quarter | year
    private String timeRange;

    // all OR actual value
    private String brand;
    private String category;
    private String region;
}
