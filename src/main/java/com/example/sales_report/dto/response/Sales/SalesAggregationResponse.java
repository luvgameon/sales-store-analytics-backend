package com.example.sales_report.dto.response.Sales;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SalesAggregationResponse {
    private String key;
    private BigDecimal totalQuantity;
    private BigDecimal totalSalesValue;
}
