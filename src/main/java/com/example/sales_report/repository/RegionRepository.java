package com.example.sales_report.repository;



import com.example.sales_report.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    @Query(
            value = """
        SELECT r.name
        from region r
        """,
            nativeQuery = true
    )
    List<String> getAllRegions();
    @Query(
            value = """
        SELECT DISTINCT p.brand
        from product p
        """,
            nativeQuery = true
    )
    List<String> getAllBrands();

    @Query(
            value = """
        SELECT DISTINCT p.category
        from product p
        """,
            nativeQuery = true
    )
    List<String> getAllCategories();
}
