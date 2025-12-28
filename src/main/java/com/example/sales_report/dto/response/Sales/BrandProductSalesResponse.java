package com.example.sales_report.dto.response.Sales;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandProductSalesResponse {

    private String product;
    private String brand;
    private Long sales;
    private Long quantity;
}
