package com.sky.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TurnoverStatisticsDTO {
    private LocalDate date;
    private Double turnover;
}
