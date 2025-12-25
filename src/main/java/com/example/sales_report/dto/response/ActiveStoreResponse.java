package com.example.sales_report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveStoreResponse {
    private String region;
    private Long activeStores;
}
