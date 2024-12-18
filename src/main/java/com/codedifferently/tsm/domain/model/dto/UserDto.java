package com.codedifferently.tsm.domain.model.dto;

import com.codedifferently.tsm.domain.model.entity.RoleEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private Integer id;
    private RoleEntity role;
    private WorksiteEntity worksite;

    private String email, phoneNumber, firstName, lastName, title;
    private Date dob;

}
