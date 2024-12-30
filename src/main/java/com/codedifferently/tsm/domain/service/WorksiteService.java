package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.domain.model.dto.WorksiteDto;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import java.util.List;

public interface WorksiteService {
    List<WorksiteEntity> getAllWorksites();
    WorksiteEntity getWorksite(Integer id);
}