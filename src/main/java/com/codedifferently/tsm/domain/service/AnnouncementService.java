package com.codedifferently.tsm.domain.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.dto.CreateAnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceCreationException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

public interface AnnouncementService {

    List<AnnouncementDto> getAllAnnouncements();

    AnnouncementDto getAnnouncement(Integer id)
            throws ResourceNotFoundException;

    void deleteAnnouncement(Integer id, Collection<GrantedAuthority> authorities) 
        throws ResourceNotFoundException, PermissionDeniedException;

    void updateAnnouncement(Integer id, CreateAnnouncementDto createAnnouncementDto, Collection<GrantedAuthority> authorities)
        throws ResourceNotFoundException, ResourceCreationException, PermissionDeniedException;

    AnnouncementEntity createAnnouncement(CreateAnnouncementDto createAnnouncementDto, Collection<GrantedAuthority> authorities)
            throws ResourceCreationException, PermissionDeniedException;

}
