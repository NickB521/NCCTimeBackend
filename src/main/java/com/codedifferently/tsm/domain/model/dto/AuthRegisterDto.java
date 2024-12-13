package com.codedifferently.tsm.domain.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AuthRegisterDto {

    private String worksite, email, password, phoneNumber,
                    firstName, lastName, jobTitle;

    private Date dob;

}
