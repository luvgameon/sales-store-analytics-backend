package com.example.sales_report.controller;


import com.example.sales_report.dto.response.ApiResponse;
import com.example.sales_report.dto.response.SalesByRegionResponse;
import com.example.sales_report.dto.response.SalesKpiResponse;
import com.example.sales_report.dto.response.TimeSeriesResponse;
import com.example.sales_report.service.SalesDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/sales")
@RequiredArgsConstructor
public class SalesDashboardController {

    private final SalesDashboardService service;

    @GetMapping("/kpis")
    public ApiResponse<SalesKpiResponse> kpis(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ApiResponse.<SalesKpiResponse> builder()
                .status("SUCCESS")
                .data(service.salesKpis(startDate, endDate))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/trend")
    public ApiResponse<List<TimeSeriesResponse>> trend(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ApiResponse.<List<TimeSeriesResponse>>builder()
                .status("SUCCESS")
                .data(service.salesTrend(startDate, endDate))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/by-region")
    public ApiResponse<List<SalesByRegionResponse>> byRegion(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ApiResponse.<List<SalesByRegionResponse>>builder()
                .status("SUCCESS")
                .data(service.salesByRegion(startDate, endDate))
                .timestamp(LocalDateTime.now())
                .build();
    }
}

