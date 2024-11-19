package com.sky.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TotalUserStatisticsDTO {
    private LocalDate date;
    private Integer total;
}
