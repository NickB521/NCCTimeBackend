package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.AuthRegisterDto;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.exception.ResourceCreationException;

public interface AuthService {

    UserEntity createUser(AuthRegisterDto registerDto) throws ResourceCreationException;

}
