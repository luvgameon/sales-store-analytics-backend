package com.example.sales_report.controller;

import com.example.sales_report.dto.response.ActiveStoresKpiResponse;
import com.example.sales_report.dto.response.ApiResponse;
import com.example.sales_report.service.StoreDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
