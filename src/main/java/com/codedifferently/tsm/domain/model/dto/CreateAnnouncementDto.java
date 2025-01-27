package com.codedifferently.tsm.domain.model.dto;

import java.util.Date;

import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import lombok.Data;

@Data
public class CreateAnnouncementDto {
    private WorksiteEntity worksite;
    private String title, message;
    private Date dateRange;
    private Boolean approved;
}
