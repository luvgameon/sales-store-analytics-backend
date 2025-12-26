package com.example.sales_report.controller;


import com.example.sales_report.dto.response.ApiResponse;

import com.example.sales_report.service.CommonService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;




@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {
    private final CommonService service;

    @GetMapping("/regions")
    public ApiResponse<List<String>> getRegions() {
        // Assuming we have a method in service to get regions
        List<String> regions  = service.getRegions();
        return new ApiResponse<>(
                "SUCCESS",
                regions,
                LocalDateTime.now()
        );
    }
    @GetMapping("/brands")
    public ApiResponse<List<String>> getBrands() {
        // Assuming we have a method in service to get regions
        List<String> brands  = service.getBrands();
        return new ApiResponse<>(
                "SUCCESS",
                brands,
                LocalDateTime.now()
        );
    }
    @GetMapping("/categories")
    public ApiResponse<List<String>> getCategories() {
        // Assuming we have a method in service to get regions
        List<String> categories  = service.getCategories();
        return new ApiResponse<>(
                "SUCCESS",
                categories,
                LocalDateTime.now()
        );
}
}

