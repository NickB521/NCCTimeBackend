package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.WorksiteDto;

import java.util.List;

public interface WorksiteService {
    List<WorksiteDto> getAllWorksites();
    WorksiteDto getWorksite(Integer id);
}