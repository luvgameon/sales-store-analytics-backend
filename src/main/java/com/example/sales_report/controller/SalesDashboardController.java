package com.example.sales_report.controller;


import com.example.sales_report.dto.request.ActiveStoreKpiRequest;
import com.example.sales_report.dto.response.*;
import com.example.sales_report.dto.response.Sales.*;
import com.example.sales_report.service.SalesDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/sales")
@RequiredArgsConstructor
public class SalesDashboardController {

    private final SalesDashboardService service;


    @PostMapping("/by-region")
    public ApiResponse<List<SalesByRegionResponse>> salesByRegion(
            @RequestBody ActiveStoreKpiRequest request) {

        return ApiResponse.<List<SalesByRegionResponse>>builder()
                .status("SUCCESS")
                .data(service.getSalesByRegion(request))
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/kpis")
    public ApiResponse<SalesKpiResponseDTO> salesKpis(
            @RequestBody ActiveStoreKpiRequest request) {

        return ApiResponse.<SalesKpiResponseDTO>builder()
                .status("SUCCESS")
                .data(service.getSalesKpis(request))
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/by-brand")
    public ApiResponse<List<SalesByBrandResponse>> salesByBrand(
            @RequestBody ActiveStoreKpiRequest request) {

        return ApiResponse.<List<SalesByBrandResponse>>builder()
                .status("SUCCESS")
                .data(service.getSalesByBrand(request))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/trendChart")
    public ApiResponse<List<SalesTrendResponse>> salesTrend(
            @RequestParam int year) {

        return ApiResponse.<List<SalesTrendResponse>>builder()
                .status("SUCCESS")
                .data(service.getSalesTrend(year))
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PostMapping("/by-brand/products")
    public ApiResponse<List<BrandProductSalesResponse>> brandProductSales(
            @RequestParam String brand,
            @RequestBody ActiveStoreKpiRequest filter) {

        return ApiResponse.<List<BrandProductSalesResponse>>builder()
                .status("SUCCESS")
                .data(service.getBrandProductSales(brand, filter))
                .timestamp(LocalDateTime.now())
                .build();
    }



}

