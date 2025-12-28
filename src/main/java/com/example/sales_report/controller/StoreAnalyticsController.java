package com.example.sales_report.controller;

import com.example.sales_report.dto.response.Store.ActiveStoreResponse;
import com.example.sales_report.dto.response.ApiResponse;
import com.example.sales_report.service.StoreAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/stores")
@RequiredArgsConstructor
public class StoreAnalyticsController {

    private final StoreAnalyticsService service;

    @GetMapping("/active")
    public ApiResponse<List<ActiveStoreResponse>> activeStores(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

        return new ApiResponse<>(
                "SUCCESS",
                service.activeStores(start, end),
                LocalDateTime.now()
        );
    }
}
