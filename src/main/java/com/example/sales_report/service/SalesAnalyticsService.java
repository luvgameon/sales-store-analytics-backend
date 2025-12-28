package com.example.sales_report.service;

import com.example.sales_report.dto.response.Sales.SalesAggregationResponse;
import com.example.sales_report.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesAnalyticsService {

    private final SalesRepository repository;

    public List<SalesAggregationResponse> monthlySales(
            int month, int year, String groupBy) {

        return repository.monthlySales(month, year, groupBy)
                .stream()
                .map(r -> new SalesAggregationResponse(
                        r[0].toString(),
                        (BigDecimal) r[1],
                        (BigDecimal) r[2]))
                .toList();
    }
}
