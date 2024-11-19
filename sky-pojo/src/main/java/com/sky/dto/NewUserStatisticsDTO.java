package com.sky.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserStatisticsDTO {
    private LocalDate date;
    private Integer total;
}
