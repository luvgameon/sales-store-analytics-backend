package com.example.sales_report.dto.response.Sales;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesByBrandResponse {

    private String brand;
    private Long sales;
    private Double growth;   // YoY %
    private Long orders;
}
