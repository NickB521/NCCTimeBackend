package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto getUser(Integer id);

}
