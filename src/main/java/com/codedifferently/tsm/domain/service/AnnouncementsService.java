package com.codedifferently.tsm.domain.service;

import java.util.List;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.dto.CreateAnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.exception.ResourceCreationException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

public interface AnnouncementsService {
    List<AnnouncementDto> getAllAnnouncements();

    AnnouncementDto getAnnouncement(Integer id)
            throws ResourceNotFoundException;
    
    //create
    AnnouncementEntity createAnnouncement(CreateAnnouncementDto createAnnouncementDto)
            throws ResourceCreationException;
}
