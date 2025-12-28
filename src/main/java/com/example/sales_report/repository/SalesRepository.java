package com.example.sales_report.repository;


import com.example.sales_report.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    // ---------------------------------------------------
    // 1️⃣ Total Sales by Brand / Product (Monthly) - JPQL
    // ---------------------------------------------------
    @Query(
            value = """
    SELECT
        CASE
            WHEN :groupBy = 'brand' THEN p.brand
            ELSE p.name
        END AS group_key,
        SUM(s.quantity) AS total_quantity,
        SUM(s.sales_value) AS total_sales
    FROM sales s
    JOIN product p ON s.product_id = p.id
    WHERE MONTH(s.sale_date) = :month
      AND YEAR(s.sale_date) = :year
    GROUP BY
        CASE
            WHEN :groupBy = 'brand' THEN p.brand
            ELSE p.name
        END
    """,
            nativeQuery = true
    )
    List<Object[]> monthlySales(
            @Param("month") int month,
            @Param("year") int year,
            @Param("groupBy") String groupBy
    );

    @Query("""
        SELECT r.name, COUNT(DISTINCT st.id)
        FROM Sales s
        JOIN s.store st
        JOIN st.region r
        WHERE s.saleDate BETWEEN :startDate AND :endDate
        GROUP BY r.name
    """)
    List<Object[]> activeStoresByRegion(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query(
            value = """
    SELECT COUNT(DISTINCT s.store_id)
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :prevStart AND :prevEnd
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    """,
            nativeQuery = true
    )
    Long totalActiveStoresPrevYear(
            LocalDate prevStart,
            LocalDate prevEnd,
            String brand,
            String category,
            String region
    );





    // 1️⃣ Active stores count per month (year)

    // 2️⃣ Store IDs per month (for new/closed stores)


    // --------------------------------------------------- Sales dashboard
    @Query(
            value = """
    SELECT 
        COALESCE(SUM(s.sales_value), 0) AS totalSales,
        COUNT(*) AS totalOrders
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :startDate AND :endDate
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    """,
            nativeQuery = true
    )
    Object[] salesSummary(
            LocalDate startDate,
            LocalDate endDate,
            String brand,
            String category,
            String region
    );
    @Query(
            value = """
    SELECT COALESCE(SUM(s.sales_value), 0)
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :prevStart AND :prevEnd
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    """,
            nativeQuery = true
    )
    Long totalSalesPrevYear(
            LocalDate prevStart,
            LocalDate prevEnd,
            String brand,
            String category,
            String region
    );
    @Query(
            value = """
    SELECT p.brand
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :startDate AND :endDate
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    GROUP BY p.brand
    ORDER BY SUM(s.sales_value) DESC
    LIMIT 1
    """,
            nativeQuery = true
    )
    String topBrandForSales(
            LocalDate startDate,
            LocalDate endDate,
            String brand,
            String category,
            String region
    );

    // ---------------------------------------------------Chart Sales Trend
    @Query(
            value = """
    SELECT 
        DATE_FORMAT(s.sale_date, '%Y-%m') AS period,
        SUM(s.sales_value) AS totalSales,
        COUNT(*) AS totalOrders
    FROM sales s
    WHERE YEAR(s.sale_date) = :year
    GROUP BY DATE_FORMAT(s.sale_date, '%Y-%m')
    ORDER BY period
    """,
            nativeQuery = true
    )
    List<Object[]> salesTrendCurrentYear(
            @Param("year") int year
    );
    @Query(
            value = """
    SELECT 
        DATE_FORMAT(s.sale_date, '%Y-%m') AS period,
        SUM(s.sales_value) AS totalSales
    FROM sales s
    WHERE YEAR(s.sale_date) = :prevYear
    GROUP BY DATE_FORMAT(s.sale_date, '%Y-%m')
    """,
            nativeQuery = true
    )
    List<Object[]> salesTrendPreviousYear(
            @Param("prevYear") int prevYear
    );


    // ---------------------------------------------------Sales by region chart
    @Query(
            value = """
    SELECT 
        r.name AS region,
        SUM(s.sales_value) AS totalSales,
        COUNT(DISTINCT s.store_id) AS stores
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :startDate AND :endDate
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:regionFilter = 'all' OR r.name = :regionFilter)
    GROUP BY r.name
    """,
            nativeQuery = true
    )
    List<Object[]> salesByRegionCurrent(
            LocalDate startDate,
            LocalDate endDate,
            String brand,
            String category,
            String regionFilter
    );
    @Query(
            value = """
    SELECT 
        r.name AS region,
        SUM(s.sales_value) AS totalSales
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :prevStart AND :prevEnd
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:regionFilter = 'all' OR r.name = :regionFilter)
    GROUP BY r.name
    """,
            nativeQuery = true
    )
    List<Object[]> salesByRegionPrevYear(
            LocalDate prevStart,
            LocalDate prevEnd,
            String brand,
            String category,
            String regionFilter
    );
    // --------------------------------------------------- Sale by brand chart
    @Query(
            value = """
    SELECT 
        p.brand AS brand,
        SUM(s.sales_value) AS totalSales,
        COUNT(*) AS totalOrders
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :startDate AND :endDate
      AND (:brandFilter = 'all' OR p.brand = :brandFilter)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    GROUP BY p.brand
    """,
            nativeQuery = true
    )
    List<Object[]> salesByBrandCurrent(
            LocalDate startDate,
            LocalDate endDate,
            String brandFilter,
            String category,
            String region
    );
    @Query(
            value = """
    SELECT 
        p.brand AS brand,
        SUM(s.sales_value) AS totalSales
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :prevStart AND :prevEnd
      AND (:brandFilter = 'all' OR p.brand = :brandFilter)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    GROUP BY p.brand
    """,
            nativeQuery = true
    )
    List<Object[]> salesByBrandPrevYear(
            LocalDate prevStart,
            LocalDate prevEnd,
            String brandFilter,
            String category,
            String region
    );

    // --------------------------------------------------- Get specific data by brand name
    @Query(
            value = """
    SELECT 
        p.name AS product,
        p.brand AS brand,
        SUM(s.sales_value) AS sales,
        SUM(s.quantity) AS quantity
    FROM sales s
    JOIN product p ON s.product_id = p.id
    WHERE p.brand = :brand
      AND s.sale_date BETWEEN :startDate AND :endDate
    GROUP BY p.name, p.brand
    ORDER BY sales DESC
    """,
            nativeQuery = true
    )
    List<Object[]> salesByBrandProducts(
            @Param("brand") String brand,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );




}
