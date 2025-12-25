package com.example.sales_report.loader;

import com.example.sales_report.entity.Region;
import com.example.sales_report.entity.Sales;
import com.example.sales_report.entity.Store;
import com.example.sales_report.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

//    private final ProductRepository productRepo;
//    private final RegionRepository regionRepo;
//    private final StoreRepository storeRepo;
    private final SalesRepository salesRepo;

    @Override
    public void run(String... args) {
//        Region north = regionRepo.save(new Region(null, "North"));
//        Store store = storeRepo.save(new Store(null, north));

//        Product p = productRepo.save(
//                new Product(null, "Soap", "Dove", "FMCG"));

//        salesRepo.save(new Sales(
//                null, p, store,
//                LocalDate.now(), 10, new BigDecimal("250.00")));
    }
}
