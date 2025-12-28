package com.example.sales_report.service;

import com.example.sales_report.dto.request.ActiveStoreKpiRequest;
import com.example.sales_report.dto.response.Store.ActiveStoresByRegionResponse;
import com.example.sales_report.dto.response.Store.ActiveStoresTrendResponse;
import com.example.sales_report.dto.response.Store.StoreDashboardKpiResponse;
import com.example.sales_report.repository.StoreRepository;
import com.example.sales_report.util.TimeRangeUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Service
@RequiredArgsConstructor
public class StoreDashboardService {

    private final StoreRepository repo;

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
    public List<ActiveStoresByRegionResponse> getActiveStoresByRegion(
            ActiveStoreKpiRequest req) {

        // Normalize filters
        String brand = req.getBrand().toLowerCase();
        String category = req.getCategory().toLowerCase();
        String regionFilter = req.getRegion().toLowerCase();

        // Resolve time range (reuse your existing util)
        LocalDate[] currRange = TimeRangeUtil.resolveRange(
                req.getStartDate(),
                req.getEndDate(),
                req.getTimeRange()
        );

        LocalDate currStart = currRange[0];
        LocalDate currEnd = currRange[1];

        LocalDate prevStart = currStart.minusYears(1);
        LocalDate prevEnd = currEnd.minusYears(1);

        // Current year data
        List<Object[]> current =
                repo.activeStoresByRegionCurrent(
                        currStart, currEnd, brand, category, regionFilter
                );

        // Previous year data
        List<Object[]> previous =
                repo.activeStoresByRegionPrevYear(
                        prevStart, prevEnd, brand, category, regionFilter
                );

        // Convert previous to map
        Map<String, Long> prevMap = previous.stream()
                .collect(Collectors.toMap(
                        r -> r[0].toString(),
                        r -> ((Number) r[1]).longValue()
                ));

        // Build response
        return current.stream()
                .map(row -> {
                    String region = row[0].toString();
                    Long currCount = ((Number) row[1]).longValue();
                    Long prevCount = prevMap.getOrDefault(region, 0L);

                    double growth = prevCount == 0
                            ? 0
                            : ((currCount - prevCount) * 100.0) / prevCount;

                    return ActiveStoresByRegionResponse.builder()
                            .region(region)
                            .activeStores(currCount)
                            .growth(Math.round(growth * 10.0) / 10.0)
                            .build();
                })
                .toList();
    }

    public List<ActiveStoresTrendResponse> getActiveStoresTrend(int year) {


        // Active store count per month
        Map<String, Long> activeCountMap =
                repo.activeStoresByMonthForYear(year)
                        .stream()
                        .collect(Collectors.toMap(
                                r -> r[0].toString(),
                                r -> ((Number) r[1]).longValue()
                        ));

        // Store IDs per month
        Map<String, Set<Long>> storeMap = new HashMap<>();

        for (Object[] row :
                repo.activeStoreIdsByMonthForYear(year)) {

            String period = row[0].toString(); // yyyy-MM
            Long storeId = ((Number) row[1]).longValue();

            storeMap.computeIfAbsent(period, k -> new HashSet<>())
                    .add(storeId);
        }

        List<String> periods = new ArrayList<>(storeMap.keySet());
        Collections.sort(periods);

        List<ActiveStoresTrendResponse> response = new ArrayList<>();

        Set<Long> previousStores = new HashSet<>();

        for (String period : periods) {

            Set<Long> currentStores = storeMap.get(period);

            Set<Long> finalPreviousStores = previousStores;
            long newStores = currentStores.stream()
                    .filter(id -> !finalPreviousStores.contains(id))
                    .count();

            long closedStores = previousStores.stream()
                    .filter(id -> !currentStores.contains(id))
                    .count();

            response.add(
                    ActiveStoresTrendResponse.builder()
                            .month(toMonthName(period))
                            .activeStores(activeCountMap.get(period))
                            .newStores(newStores)
                            .closedStores(closedStores)
                            .build()
            );

            previousStores = currentStores;
        }

        return response;
    }
    private String toMonthName(String period) {
        int month = Integer.parseInt(period.substring(5));
        return Month.of(month).name().substring(0, 1)
                + Month.of(month).name().substring(1, 3).toLowerCase();
    }


}
