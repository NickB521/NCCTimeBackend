package com.codedifferently.tsm.domain.service.impl;

import com.codedifferently.tsm.domain.model.dto.WorksiteDto;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import com.codedifferently.tsm.domain.repository.WorksiteRepository;
import com.codedifferently.tsm.domain.service.WorksiteService;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorksiteServiceImpl implements WorksiteService {
    private final WorksiteRepository worksiteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WorksiteServiceImpl(WorksiteRepository worksiteRepository, ModelMapper modelMapper) {
        this.worksiteRepository = worksiteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<WorksiteEntity> getAllWorksites() {
        List<WorksiteEntity> worksiteEntities = worksiteRepository.findAll();

        return worksiteEntities;
    }

    @Override
    public WorksiteEntity getWorksite(Integer id) {
        Optional<WorksiteEntity> worksite = worksiteRepository.findById(id);
        if (worksite.isEmpty()) throw new ResourceNotFoundException("Worksite not found");

        return worksite.get();
        // WorksiteEntity worksite = worksiteRepository.findById(id)
        //         .orElseThrow(() -> new ResourceNotFoundException("Worksite not found"));
        // return modelMapper.map(worksite, WorksiteDto.class);
    }

    private WorksiteDto mapSites(WorksiteEntity worksiteEntity) {
        return modelMapper.map(worksiteEntity, WorksiteDto.class);
    }
}