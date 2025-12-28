package com.example.sales_report.controller;

import com.example.sales_report.dto.response.ApiResponse;
import com.example.sales_report.dto.response.Sales.SalesAggregationResponse;
import com.example.sales_report.service.SalesAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/sales")
@RequiredArgsConstructor
public class SalesAnalyticsController {

    private final SalesAnalyticsService service;

    @GetMapping("/monthly")
    public ApiResponse<List<SalesAggregationResponse>> monthly(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam String brandName) {

        return new ApiResponse<>(
                "SUCCESS",
                service.monthlySales(month, year, brandName),
                LocalDateTime.now()
        );
    }
}
