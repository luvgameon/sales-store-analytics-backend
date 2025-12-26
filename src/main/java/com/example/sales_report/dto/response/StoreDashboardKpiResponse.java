package com.example.sales_report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class StoreDashboardKpiResponse {
    private Long totalActiveStores;
    private Double yoyChangePercent;
    private Long newStores;
    private String topRegion;
}
