package com.example.sales_report.repository;

import com.example.sales_report.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query(
            value = """
    SELECT COUNT(DISTINCT s.store_id)
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
    Long totalActiveStores(
            LocalDate startDate,
            LocalDate endDate,
            String brand,
            String category,
            String region
    );
    @Query(
            value = """
    SELECT COUNT(DISTINCT curr.store_id)
    FROM (
        SELECT DISTINCT s.store_id
        FROM sales s
        JOIN product p ON s.product_id = p.id
        JOIN store st ON s.store_id = st.id
        JOIN region r ON st.region_id = r.id
        WHERE s.sale_date BETWEEN :startDate AND :endDate
          AND (:brand = 'all' OR p.brand = :brand)
          AND (:category = 'all' OR p.category = :category)
          AND (:region = 'all' OR r.name = :region)
    ) curr
    LEFT JOIN (
        SELECT DISTINCT s.store_id
        FROM sales s
        JOIN product p ON s.product_id = p.id
        JOIN store st ON s.store_id = st.id
        JOIN region r ON st.region_id = r.id
        WHERE s.sale_date BETWEEN :prevStart AND :prevEnd
          AND (:brand = 'all' OR p.brand = :brand)
          AND (:category = 'all' OR p.category = :category)
          AND (:region = 'all' OR r.name = :region)
    ) prev
    ON curr.store_id = prev.store_id
    WHERE prev.store_id IS NULL
    """,
            nativeQuery = true
    )
    Long newStores(
            LocalDate startDate,
            LocalDate endDate,
            LocalDate prevStart,
            LocalDate prevEnd,
            String brand,
            String category,
            String region
    );
    @Query(
            value = """
    SELECT r.name
    FROM sales s
    JOIN product p ON s.product_id = p.id
    JOIN store st ON s.store_id = st.id
    JOIN region r ON st.region_id = r.id
    WHERE s.sale_date BETWEEN :startDate AND :endDate
      AND (:brand = 'all' OR p.brand = :brand)
      AND (:category = 'all' OR p.category = :category)
      AND (:region = 'all' OR r.name = :region)
    GROUP BY r.name
    ORDER BY COUNT(DISTINCT s.store_id) DESC
    LIMIT 1
    """,
            nativeQuery = true
    )
    String topRegion(
            LocalDate startDate,
            LocalDate endDate,
            String brand,
            String category,
            String region
    );
    @Query(
            value = """
    SELECT r.name AS region,
           COUNT(DISTINCT s.store_id) AS activeStores
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
    List<Object[]> activeStoresByRegionCurrent(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("brand") String brand,
            @Param("category") String category,
            @Param("regionFilter") String regionFilter
    );
    @Query(
            value = """
    SELECT r.name AS region,
           COUNT(DISTINCT s.store_id) AS activeStores
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
    List<Object[]> activeStoresByRegionPrevYear(
            @Param("prevStart") LocalDate prevStart,
            @Param("prevEnd") LocalDate prevEnd,
            @Param("brand") String brand,
            @Param("category") String category,
            @Param("regionFilter") String regionFilter
    );
    @Query(
            value = """
    SELECT 
        DATE_FORMAT(s.sale_date, '%Y-%m') AS period,
        COUNT(DISTINCT s.store_id) AS activeStores
    FROM sales s
    WHERE YEAR(s.sale_date) = :year
    GROUP BY DATE_FORMAT(s.sale_date, '%Y-%m')
    ORDER BY period
    """,
            nativeQuery = true
    )
    List<Object[]> activeStoresByMonthForYear(
            @Param("year") int year
    );
    @Query(
            value = """
    SELECT 
        DATE_FORMAT(s.sale_date, '%Y-%m') AS period,
        s.store_id
    FROM sales s
    WHERE YEAR(s.sale_date) = :year
    GROUP BY DATE_FORMAT(s.sale_date, '%Y-%m'), s.store_id
    """,
            nativeQuery = true
    )
    List<Object[]> activeStoreIdsByMonthForYear(
            @Param("year") int year
    );
}
