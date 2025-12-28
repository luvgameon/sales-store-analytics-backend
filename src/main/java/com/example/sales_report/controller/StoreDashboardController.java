package com.example.sales_report.controller;

import com.example.sales_report.dto.request.ActiveStoreKpiRequest;
import com.example.sales_report.dto.response.*;
import com.example.sales_report.dto.response.Store.ActiveStoresByRegionResponse;
import com.example.sales_report.dto.response.Store.ActiveStoresTrendResponse;
import com.example.sales_report.dto.response.Store.StoreDashboardKpiResponse;
import com.example.sales_report.service.StoreDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/stores")
@RequiredArgsConstructor
public class StoreDashboardController {

    private final StoreDashboardService service;

    @GetMapping("/trend")
    public ApiResponse<List<ActiveStoresTrendResponse>> activeStoresTrend(
            @RequestParam int year) {

        return ApiResponse.<List<ActiveStoresTrendResponse>>builder()
                .status("SUCCESS")
                .data(service.getActiveStoresTrend(year))
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/kpis")
    public ApiResponse<StoreDashboardKpiResponse> storeKpis(
            @RequestBody ActiveStoreKpiRequest request) {

        return ApiResponse.<StoreDashboardKpiResponse>builder()
                .status("SUCCESS")
                .data(service.getStoreKpis(request))
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/by-region")
    public ApiResponse<List<ActiveStoresByRegionResponse>> activeStoresByRegion(
            @RequestBody ActiveStoreKpiRequest request) {

        return ApiResponse.<List<ActiveStoresByRegionResponse>>builder()
                .status("SUCCESS")
                .data(service.getActiveStoresByRegion(request))
                .timestamp(LocalDateTime.now())
                .build();
    }

}
