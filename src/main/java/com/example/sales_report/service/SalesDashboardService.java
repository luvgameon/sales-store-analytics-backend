package com.example.sales_report.service;

import com.example.sales_report.dto.response.SalesByRegionResponse;
import com.example.sales_report.dto.response.SalesKpiResponse;
import com.example.sales_report.dto.response.TimeSeriesResponse;
import com.example.sales_report.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesDashboardService {

    private final SalesRepository repo;

    public SalesKpiResponse salesKpis(LocalDate start, LocalDate end) {

        BigDecimal totalSales = repo.totalSales(start, end);

        Object[] top = repo.topBrand(start, end);
        String topBrand =
                top != null && top.length > 0 && top[0] instanceof Object[]
                        ? ((Object[]) top[0])[0].toString()
                        : "NA";

        // YoY calculation (simplified)
        BigDecimal prevYearSales =
                repo.totalSales(start.minusYears(1), end.minusYears(1));

        double yoy = prevYearSales.compareTo(BigDecimal.ZERO) == 0
                ? 0
                : totalSales.subtract(prevYearSales)
                .divide(prevYearSales, 2, BigDecimal.ROUND_HALF_UP)
                .doubleValue() * 100;

        return SalesKpiResponse.builder()
                .totalSales(totalSales)
                .topBrand(topBrand)
                .yoyGrowthPercent(yoy)
                .build();
    }

    public List<TimeSeriesResponse> salesTrend (LocalDate start, LocalDate end) {
        return repo.salesTrend(start, end).stream()
                .map(r -> new TimeSeriesResponse(
                        r[0].toString(),
                        (BigDecimal) r[1]))
                .toList();
    }

    public List<SalesByRegionResponse> salesByRegion(
            LocalDate start, LocalDate end) {

        return repo.salesByRegion(start, end).stream()
                .map(r -> new SalesByRegionResponse(
                        r[0].toString(),
                        (BigDecimal) r[1]))
                .toList();
    }
}

