package com.codedifferently.tsm.domain.service.impl;
import com.codedifferently.tsm.domain.model.dto.WorksiteDto;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import com.codedifferently.tsm.domain.repository.WorksiteRepository;
import com.codedifferently.tsm.domain.service.WorksiteService;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorksiteServiceImpl implements WorksiteService {
    private final WorksiteRepository worksiteRepository;
    private final ModelMapper mapper;

    @Autowired
    public WorksiteServiceImpl(WorksiteRepository worksiteRepository, ModelMapper mapper) {
        this.worksiteRepository = worksiteRepository;
        this.mapper = mapper;
    }

    @Override
    public List<WorksiteDto> getAllWorksites() {
        return worksiteRepository.findAll().stream()
                .map(worksite -> mapper.map(worksite, WorksiteDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public WorksiteDto getWorksite(Integer id) {
        WorksiteEntity worksite = worksiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worksite not found"));
        return mapper.map(worksite, WorksiteDto.class);
    }
}