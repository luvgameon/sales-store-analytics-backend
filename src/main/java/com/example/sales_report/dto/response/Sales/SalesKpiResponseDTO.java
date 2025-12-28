package com.example.sales_report.dto.response.Sales;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesKpiResponseDTO {

    private Long totalSales;
    private Long totalOrders;
    private Long avgOrderValue;
    private String topBrand;
    private Double yoyGrowth;
}