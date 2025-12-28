package com.example.sales_report.dto.response.Store;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveStoresKpiResponse {
    private Long totalActiveStores;
    private Double yoyChangePercent;
}
