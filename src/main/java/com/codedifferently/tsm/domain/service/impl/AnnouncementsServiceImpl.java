package com.codedifferently.tsm.domain.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.domain.repository.AnnouncementRepository;
import com.codedifferently.tsm.domain.service.AnnouncementsService;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

@Service
public class AnnouncementsServiceImpl implements AnnouncementsService{
    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AnnouncementsServiceImpl(AnnouncementRepository announcementRepository, ModelMapper modelMapper) {
        this.announcementRepository = announcementRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AnnouncementDto> getAllAnnouncements() {
        List<AnnouncementEntity> announcementEntities = announcementRepository.findAll();

        return announcementEntities.stream()
                .map(this::mapSites)
                .collect(Collectors.toList());
    }

    @Override
    public AnnouncementDto getAnnouncement(Integer id)
            throws ResourceNotFoundException {

        Optional<AnnouncementEntity> announcement = announcementRepository.findById(id);
        if (announcement.isEmpty()) throw new ResourceNotFoundException("Announcement not found");

        return mapSites(announcement.get());
    }

    private AnnouncementDto mapSites(AnnouncementEntity announcementEntity) {
        return modelMapper.map(announcementEntity, AnnouncementDto.class);
    }


}
