package com.example.sales_report.service;

import com.example.sales_report.dto.request.ActiveStoreKpiRequest;
import com.example.sales_report.dto.response.ActiveStoresKpiResponse;
import com.example.sales_report.dto.response.StoreDashboardKpiResponse;
import com.example.sales_report.repository.SalesRepository;
import com.example.sales_report.util.TimeRangeUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Builder
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
    public StoreDashboardKpiResponse getStoreKpis(ActiveStoreKpiRequest req) {

        String brand = req.getBrand().toLowerCase();
        String category = req.getCategory().toLowerCase();
        String region = req.getRegion().toLowerCase();

        // Resolve current period
        LocalDate[] currentRange = TimeRangeUtil.resolveRange(
                req.getStartDate(),
                req.getEndDate(),
                req.getTimeRange()
        );


        LocalDate currStart = currentRange[0];
        LocalDate currEnd = currentRange[1];

        // Resolve previous year period
        LocalDate prevStart =  currStart.minusYears(1);
        LocalDate prevEnd = currEnd.minusYears(1);

        Long current = repo.totalActiveStores(
                currStart, currEnd,
                brand, category, region
        );

        Long previous =
                repo.totalActiveStores(
                        prevStart, prevEnd,
                        brand, category, region
                );

        double yoy = (previous == 0)
                ? 0
                : ((current - previous) * 100.0) / previous;

        Long newStores =
                repo.newStores(
                        currStart, currEnd,
                        prevStart, prevEnd,
                        brand, category, region
                );

        String topRegion = repo.topRegion(
                currStart, currEnd,
                brand, category, region
        );

        return new StoreDashboardKpiResponse(current, yoy, newStores, topRegion);
    }
}
