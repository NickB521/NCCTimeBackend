package com.codedifferently.tsm.domain.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HolidayDto {

    private Integer id;
    private String title, type;
    private Date start, end;

}