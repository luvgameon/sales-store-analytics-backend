package com.example.sales_report.dto.response;


import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSeriesResponse {
    private String period;
    private BigDecimal value;
}
