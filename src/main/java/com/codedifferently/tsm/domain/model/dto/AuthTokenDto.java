package com.codedifferently.tsm.domain.model.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AuthTokenDto {

    private String token, type;

}
