package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.WorksiteDto;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import java.util.List;

public interface WorksiteService {

    List<WorksiteDto> getAllWorksites();

    WorksiteDto getWorksite(Integer id)
            throws ResourceNotFoundException;

}