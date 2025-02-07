package com.codedifferently.tsm.domain.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CreateAnnouncementDto {

    private Integer worksiteId;
    private String title, message;
    private Date start, end;
    private Boolean approved;

}
