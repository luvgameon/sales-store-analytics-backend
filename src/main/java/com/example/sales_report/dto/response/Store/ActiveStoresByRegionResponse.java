package com.example.sales_report.dto.response.Store;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveStoresByRegionResponse {
    private String region;
    private Long activeStores;
    private Double growth; // YoY %
}
