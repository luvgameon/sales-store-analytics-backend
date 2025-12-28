package com.example.sales_report.dto.response.Store;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveStoresTrendResponse {

    private String month;
    private Long activeStores;
    private Long newStores;
    private Long closedStores;
}
