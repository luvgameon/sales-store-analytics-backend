package com.example.sales_report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Sales_Report APIS API's",
                version = "1.0",
                description = "API documentation for homoMed"
        )
)
@SpringBootApplication
public class SalesReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesReportApplication.class, args);
	}

}
