package com.example.sales_report.service;

import com.example.sales_report.dto.response.ActiveStoreResponse;
import com.example.sales_report.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreAnalyticsService {

    private final SalesRepository repository;

    public List<ActiveStoreResponse> activeStores(
            LocalDate start, LocalDate end) {

        return repository.activeStoresByRegion(start, end)
                .stream()
                .map(r -> new ActiveStoreResponse(
                        r[0].toString(),
                        (Long) r[1]))
                .toList();
    }
}
