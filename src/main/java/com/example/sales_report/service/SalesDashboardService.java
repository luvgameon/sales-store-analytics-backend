package com.example.sales_report.service;

import com.example.sales_report.dto.request.ActiveStoreKpiRequest;
import com.example.sales_report.dto.response.Sales.*;
import com.example.sales_report.repository.SalesRepository;
import com.example.sales_report.util.TimeRangeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesDashboardService {

    private final SalesRepository repo;

    private String toMonthName(String period) {
        int month = Integer.parseInt(period.substring(5));
        return Month.of(month)
                .name()
                .substring(0, 1)
                + Month.of(month).name().substring(1, 3).toLowerCase();
    }

    public SalesKpiResponseDTO getSalesKpis(ActiveStoreKpiRequest req) {

        String brand = req.getBrand().toLowerCase();
        String category = req.getCategory().toLowerCase();
        String region = req.getRegion().toLowerCase();

        // Resolve date range (reuse your existing util)
        LocalDate[] range = TimeRangeUtil.resolveRange(
                req.getStartDate(),
                req.getEndDate(),
                req.getTimeRange()
        );

        LocalDate start = range[0];
        LocalDate end = range[1];

        LocalDate prevStart = start.minusYears(1);
        LocalDate prevEnd = end.minusYears(1);

        // Current period summary
        Object[] summary = repo.salesSummary(
                start, end, brand, category, region
        );
        Object[] row = (Object[]) summary[0];

        long totalSales = ((Number) row[0]).longValue();
        long totalOrders = ((Number) row[1]).longValue();


        long avgOrderValue =
                totalOrders == 0 ? 0 : totalSales / totalOrders;

        // Previous year sales
        long prevSales = repo.totalSalesPrevYear(
                prevStart, prevEnd, brand, category, region
        );

        double yoyGrowth =
                prevSales == 0
                        ? 0
                        : ((totalSales - prevSales) * 100.0) / prevSales;

        // Top brand
        String topBrand = repo.topBrandForSales(
                start, end, brand, category, region
        );

        return SalesKpiResponseDTO.builder()
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .avgOrderValue(avgOrderValue)
                .topBrand(topBrand)
                .yoyGrowth(Math.round(yoyGrowth * 10.0) / 10.0)
                .build();
    }
    public List<SalesTrendResponse> getSalesTrend(int year) {

        int prevYear = year - 1;

        // Current year data
        List<Object[]> currentRows =
                repo.salesTrendCurrentYear(year);

        // Previous year data
        List<Object[]> prevRows =
                repo.salesTrendPreviousYear(prevYear);

        // Map previous year sales by month
        Map<String, Long> prevSalesMap = prevRows.stream()
                .collect(Collectors.toMap(
                        r -> r[0].toString().substring(5), // MM
                        r -> ((Number) r[1]).longValue()
                ));

        List<SalesTrendResponse> response = new ArrayList<>();

        for (Object[] row : currentRows) {

            String period = row[0].toString(); // yyyy-MM
            String month = toMonthName(period);

            long currentSales = ((Number) row[1]).longValue();
            long orders = ((Number) row[2]).longValue();
            long lastYearSales = prevSalesMap.getOrDefault(
                    period.substring(5), 0L
            );

            response.add(
                    SalesTrendResponse.builder()
                            .month(month)
                            .sales(currentSales)
                            .lastYear(lastYearSales)
                            .orders(orders)
                            .build()
            );
        }

        return response;
    }
    public List<SalesByRegionResponse> getSalesByRegion(
            ActiveStoreKpiRequest req) {

        String brand = req.getBrand().toLowerCase();
        String category = req.getCategory().toLowerCase();
        String regionFilter = req.getRegion().toLowerCase();

        // Resolve date range (reuse your existing util)
        LocalDate[] range = TimeRangeUtil.resolveRange(
                req.getStartDate(),
                req.getEndDate(),
                req.getTimeRange()
        );

        LocalDate start = range[0];
        LocalDate end = range[1];

        LocalDate prevStart = start.minusYears(1);
        LocalDate prevEnd = end.minusYears(1);

        // Current year data
        List<Object[]> current =
                repo.salesByRegionCurrent(
                        start, end, brand, category, regionFilter
                );

        // Previous year data
        List<Object[]> previous =
                repo.salesByRegionPrevYear(
                        prevStart, prevEnd, brand, category, regionFilter
                );

        // Map previous year sales
        Map<String, Long> prevSalesMap = previous.stream()
                .collect(Collectors.toMap(
                        r -> r[0].toString(),
                        r -> ((Number) r[1]).longValue()
                ));

        // Build response
        return current.stream()
                .map(row -> {
                    String region = row[0].toString();
                    long sales = ((Number) row[1]).longValue();
                    long stores = ((Number) row[2]).longValue();

                    long prevSales = prevSalesMap.getOrDefault(region, 0L);

                    double growth = prevSales == 0
                            ? 0
                            : ((sales - prevSales) * 100.0) / prevSales;

                    return SalesByRegionResponse.builder()
                            .region(region)
                            .sales(sales)
                            .stores(stores)
                            .growth(Math.round(growth * 10.0) / 10.0)
                            .build();
                })
                .toList();
    }
    public List<SalesByBrandResponse> getSalesByBrand(
            ActiveStoreKpiRequest req) {

        String brandFilter = req.getBrand().toLowerCase();
        String category = req.getCategory().toLowerCase();
        String region = req.getRegion().toLowerCase();

        // Resolve date range (your existing util)
        LocalDate[] range = TimeRangeUtil.resolveRange(
                req.getStartDate(),
                req.getEndDate(),
                req.getTimeRange()
        );

        LocalDate start = range[0];
        LocalDate end = range[1];

        LocalDate prevStart = start.minusYears(1);
        LocalDate prevEnd = end.minusYears(1);

        // Current period
        List<Object[]> current =
                repo.salesByBrandCurrent(
                        start, end, brandFilter, category, region
                );

        // Previous year
        List<Object[]> previous =
                repo.salesByBrandPrevYear(
                        prevStart, prevEnd, brandFilter, category, region
                );

        // Map previous sales by brand
        Map<String, Long> prevSalesMap = previous.stream()
                .collect(Collectors.toMap(
                        r -> r[0].toString(),
                        r -> ((Number) r[1]).longValue()
                ));

        // Build response
        return current.stream()
                .map(row -> {
                    String brand = row[0].toString();
                    long sales = ((Number) row[1]).longValue();
                    long orders = ((Number) row[2]).longValue();

                    long prevSales = prevSalesMap.getOrDefault(brand, 0L);

                    double growth = prevSales == 0
                            ? 0
                            : ((sales - prevSales) * 100.0) / prevSales;

                    return SalesByBrandResponse.builder()
                            .brand(brand)
                            .sales(sales)
                            .orders(orders)
                            .growth(Math.round(growth * 10.0) / 10.0)
                            .build();
                })
                .toList();
    }

    public List<BrandProductSalesResponse> getBrandProductSales(
            String brand,
            ActiveStoreKpiRequest filter) {

        // Resolve date range (your existing util)
        LocalDate[] range = TimeRangeUtil.resolveRange(
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getTimeRange()
        );

        LocalDate startDate = range[0];
        LocalDate endDate = range[1];


        List<Object[]> rows =
                repo.salesByBrandProducts(
                        brand, startDate, endDate
                );

        return rows.stream()
                .map(row -> BrandProductSalesResponse.builder()
                        .product(row[0].toString())
                        .brand(row[1].toString())
                        .sales(((Number) row[2]).longValue())
                        .quantity(((Number) row[3]).longValue())
                        .build()
                )
                .toList();
    }



}

