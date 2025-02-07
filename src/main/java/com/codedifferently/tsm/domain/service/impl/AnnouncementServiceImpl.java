package com.codedifferently.tsm.domain.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.dto.CreateAnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.domain.repository.AnnouncementRepository;
import com.codedifferently.tsm.domain.repository.WorksiteRepository;
import com.codedifferently.tsm.domain.service.AnnouncementService;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceCreationException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final WorksiteRepository worksiteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, ModelMapper modelMapper, WorksiteRepository worksiteRepository) {
        this.announcementRepository = announcementRepository;
        this.worksiteRepository = worksiteRepository;
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

    @Override
    public void deleteAnnouncement(Integer id, Collection<GrantedAuthority> authorities) throws ResourceNotFoundException, PermissionDeniedException {
        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("EMPLOYEE"))) {
            throw new PermissionDeniedException("Permission denied");
        }

        if (announcementRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Announcement not found");
        }

        announcementRepository.deleteById(id);
    }

    @Override
    public void updateAnnouncement(Integer id, CreateAnnouncementDto createAnnouncementDto, Collection<GrantedAuthority> authorities) throws ResourceCreationException, PermissionDeniedException, ResourceNotFoundException {
        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("EMPLOYEE"))) {
            throw new PermissionDeniedException("Permission denied");
        }

        if (announcementRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Announcement not found");
        }

        AnnouncementEntity existingAnnouncement = announcementRepository.findById(id).get();
        existingAnnouncement.setApproved(createAnnouncementDto.getApproved());
        existingAnnouncement.setMessage(createAnnouncementDto.getMessage());
        existingAnnouncement.setTitle(createAnnouncementDto.getTitle());
        existingAnnouncement.setDateRange(createAnnouncementDto.getDateRange());

        announcementRepository.save(existingAnnouncement);
    }

    @Override
    public AnnouncementEntity createAnnouncement(CreateAnnouncementDto createAnnouncementDto, Collection<GrantedAuthority> authorities) throws ResourceCreationException, PermissionDeniedException {
        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("EMPLOYEE"))) {
            throw new PermissionDeniedException("Permission denied");
        }

        AnnouncementEntity announcementEntity = new AnnouncementEntity();

        if (createAnnouncementDto.getWorksiteId() != null) {
            Optional<WorksiteEntity> worksiteEntity = worksiteRepository.findById(createAnnouncementDto.getWorksiteId());
            if (worksiteEntity.isEmpty()) throw new ResourceNotFoundException("Worksite not found");

            announcementEntity.setWorksite(worksiteEntity.get());
        }

        announcementEntity.setApproved(createAnnouncementDto.getApproved());
        announcementEntity.setMessage(createAnnouncementDto.getMessage());
        announcementEntity.setTitle(createAnnouncementDto.getTitle());
        announcementEntity.setDateRange(createAnnouncementDto.getDateRange());

        return announcementEntity;
    }

    private AnnouncementDto mapSites(AnnouncementEntity announcementEntity) {
        return modelMapper.map(announcementEntity, AnnouncementDto.class);
    }

}
