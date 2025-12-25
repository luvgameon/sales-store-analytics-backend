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

    // ---------------------------------------------------
    // 2️⃣ Active Stores by Region - JPQL (SAFE)
    // ---------------------------------------------------
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

    // ---------------------------------------------------
    // 3️⃣ KPI: Total Sales - JPQL (SAFE)
    // ---------------------------------------------------
    @Query("""
        SELECT COALESCE(SUM(s.salesValue), 0)
        FROM Sales s
        WHERE s.saleDate BETWEEN :startDate AND :endDate
    """)
    BigDecimal totalSales(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ---------------------------------------------------
    // 4️⃣ KPI: Top Brand - NATIVE QUERY (LIMIT ❗)
    // ---------------------------------------------------
    @Query(
            value = """
        SELECT p.brand, SUM(s.sales_value) AS total
        FROM sales s
        JOIN product p ON s.product_id = p.id
        WHERE s.sale_date BETWEEN :startDate AND :endDate
        GROUP BY p.brand
        ORDER BY total DESC
        LIMIT 1
        """,
            nativeQuery = true
    )
    Object[] topBrand(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ---------------------------------------------------
    // 5️⃣ Sales Trend (Monthly) - NATIVE QUERY (DATE FORMAT ❗)
    // ---------------------------------------------------
    @Query(
            value = """
        SELECT 
            DATE_FORMAT(s.sale_date, '%Y-%m') AS period,
            SUM(s.sales_value) AS totalSales
        FROM sales s
        WHERE s.sale_date BETWEEN :startDate AND :endDate
        GROUP BY DATE_FORMAT(s.sale_date, '%Y-%m')
        ORDER BY period
        """,
            nativeQuery = true
    )
    List<Object[]> salesTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ---------------------------------------------------
    // 6️⃣ Sales by Region - JPQL (SAFE)
    // ---------------------------------------------------
    @Query("""
        SELECT r.name, SUM(s.salesValue)
        FROM Sales s
        JOIN s.store st
        JOIN st.region r
        WHERE s.saleDate BETWEEN :startDate AND :endDate
        GROUP BY r.name
    """)
    List<Object[]> salesByRegion(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ---------------------------------------------------
    // 7️⃣ Drill-down: Brand → Product - JPQL (SAFE)
    // ---------------------------------------------------
    @Query("""
        SELECT p.name, SUM(s.salesValue)
        FROM Sales s
        JOIN s.product p
        WHERE p.brand = :brand
          AND s.saleDate BETWEEN :startDate AND :endDate
        GROUP BY p.name
        ORDER BY SUM(s.salesValue) DESC
    """)
    List<Object[]> salesByProductForBrand(
            @Param("brand") String brand,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
