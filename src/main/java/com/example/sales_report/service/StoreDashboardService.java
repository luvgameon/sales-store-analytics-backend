package com.example.sales_report.service;

import com.example.sales_report.dto.response.ActiveStoresKpiResponse;
import com.example.sales_report.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StoreDashboardService {

    private final SalesRepository repo;

    public ActiveStoresKpiResponse activeStoresKpi(
            LocalDate start, LocalDate end) {

        Long current = repo.activeStoresByRegion(start, end)
                .stream().mapToLong(r -> (Long) r[1]).sum();

        Long prev = repo.activeStoresByRegion(
                        start.minusYears(1), end.minusYears(1))
                .stream().mapToLong(r -> (Long) r[1]).sum();

        double yoy = prev == 0 ? 0 : ((current - prev) * 100.0) / prev;

        return new ActiveStoresKpiResponse(current, yoy);
    }
}
