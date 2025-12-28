package com.example.sales_report.dto.response.Sales;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesByRegionResponse {
    private String region;
    private Long sales;      // total sales value
    private Double growth;   // YoY %
    private Long stores;
}