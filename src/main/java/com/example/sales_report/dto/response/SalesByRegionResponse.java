package com.example.sales_report.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesByRegionResponse {
    private String region;
    private BigDecimal salesValue;
}