package com.codedifferently.tsm.domain.model.dto;

import java.util.Date;

import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import lombok.Data;

@Data
public class AnnouncementDto {

    private WorksiteEntity worksite;
    private Integer id;
    private String title, message;
    private Date start, end;
    private Boolean approved;

}
