package com.example.sales_report.dto.response.Sales;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesTrendResponse {

    private String month;     // Jan, Feb, Mar
    private Long sales;       // current year sales
    private Long lastYear;    // previous year sales
    private Long orders;      // current year orders
}
