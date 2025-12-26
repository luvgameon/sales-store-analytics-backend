package com.example.sales_report.controller;

import com.example.sales_report.dto.request.ActiveStoreKpiRequest;
import com.example.sales_report.dto.response.ActiveStoresKpiResponse;
import com.example.sales_report.dto.response.ApiResponse;
import com.example.sales_report.dto.response.StoreDashboardKpiResponse;
import com.example.sales_report.service.StoreDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard/stores")
@RequiredArgsConstructor
public class StoreDashboardController {

    private final StoreDashboardService service;

    @GetMapping("/kpis")
    public ApiResponse<ActiveStoresKpiResponse> kpis(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ApiResponse.<ActiveStoresKpiResponse> builder()
                .status("SUCCESS")
                .data(service.activeStoresKpi(startDate, endDate))
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
}
