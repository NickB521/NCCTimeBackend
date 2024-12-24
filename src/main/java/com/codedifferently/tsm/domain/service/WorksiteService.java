package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.UserDto;

import java.util.List;

public interface WorksiteService {
    List<UserDto> getAllWorksites();
    UserDto getWorksite(Integer id);
}