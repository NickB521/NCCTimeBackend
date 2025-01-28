package com.codedifferently.tsm.domain.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CreateAnnouncementDto {
    private Long worksiteId;
    private String title, message;
    private Date dateRange;
    private Boolean approved;
}
