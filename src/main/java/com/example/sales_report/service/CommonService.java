package com.example.sales_report.service;

import com.example.sales_report.dto.response.ActiveStoreResponse;
import com.example.sales_report.dto.response.SalesAggregationResponse;
import com.example.sales_report.repository.RegionRepository;
import com.example.sales_report.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final RegionRepository regionRepo;

    public List<String> getRegions() {

        return regionRepo.getAllRegions()
                .stream().toList();
    }
    public List<String> getBrands() {

        return regionRepo.getAllBrands()
                .stream().toList();
    }
    public List<String> getCategories() {

        return regionRepo.getAllCategories()
                .stream().toList();
    }

}




