package com.codedifferently.tsm;

import com.codedifferently.tsm.domain.controller.AnnouncementController;
import com.codedifferently.tsm.domain.model.dto.AnnouncementDto;
import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import com.codedifferently.tsm.domain.repository.AnnouncementRepository;
import com.codedifferently.tsm.domain.service.impl.AnnouncementServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AnnouncementTests {

    @Nested
    @DisplayName("Announcement Controller Tests")
    class AnnouncementControllerTests {
        @Mock
        private AnnouncementServiceImpl announcementService;
        private AnnouncementController announcementController;
        private AnnouncementDto announcementDto;
        private AnnouncementRepository announcementRepository;

        @BeforeEach
        void setUp() {
            announcementController = new AnnouncementController(announcementService, announcementRepository);
            announcementDto = createMockAnnouncementDto();
        }
        @Test
        @DisplayName("Handle Resource Not Found Exception")
        void handleResourceNotFoundException() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Test exception");
            ResponseEntity<String> response = announcementController.handleResourceNotFoundException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Test exception", response.getBody());
        }

        @Test
        @DisplayName("Get All Announcements Empty List")
        void getAllAnnouncements_EmptyList() {
            when(announcementService.getAllAnnouncements()).thenReturn(new ArrayList<>());

            ResponseEntity<List<AnnouncementDto>> response = announcementController.all();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isEmpty());
            verify(announcementService).getAllAnnouncements();
        }

        @Test
        @DisplayName("Get All Announcements Success")
        void getAllAnnouncements_Success() {
            List<AnnouncementDto> announcements = Arrays.asList(announcementDto);
            when(announcementService.getAllAnnouncements()).thenReturn(announcements);

            ResponseEntity<List<AnnouncementDto>> response = announcementController.all();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            verify(announcementService).getAllAnnouncements();
        }

        @Test
        @DisplayName("Get Announcement By ID Success")
        void getAnnouncementById_Success() throws ResourceNotFoundException {
            when(announcementService.getAnnouncement(1)).thenReturn(announcementDto);

            ResponseEntity<AnnouncementDto> response = announcementController.announcement(1);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(announcementDto.getTitle(), response.getBody().getTitle());
            verify(announcementService).getAnnouncement(1);
        }

        @Test
        @DisplayName("Get Announcement By ID Not Found")
        void getAnnouncementById_NotFound() throws ResourceNotFoundException {
            when(announcementService.getAnnouncement(999))
                    .thenThrow(new ResourceNotFoundException("Announcement not found"));

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> announcementController.announcement(999));

            assertEquals("Announcement not found", exception.getMessage());
            verify(announcementService).getAnnouncement(999);
        }
    }


    @Nested
    @DisplayName("Announcement Service Tests")
    class AnnouncementServiceTests {
        @Mock
        private AnnouncementRepository announcementRepository;
        @Mock
        private ModelMapper modelMapper;
        @InjectMocks
        private AnnouncementServiceImpl announcementService;

        private AnnouncementEntity announcementEntity;
        private AnnouncementDto announcementDto;

        @BeforeEach
        void setUp() {
            announcementEntity = createMockAnnouncementEntity();
            announcementDto = createMockAnnouncementDto();
        }

        @Test
        @DisplayName("Get All Announcements Service Success")
        void getAllAnnouncements_Success() {
            List<AnnouncementEntity> entities = Arrays.asList(announcementEntity);
            when(announcementRepository.findAll()).thenReturn(entities);
            when(modelMapper.map(any(AnnouncementEntity.class), eq(AnnouncementDto.class)))
                    .thenReturn(announcementDto);

            List<AnnouncementDto> result = announcementService.getAllAnnouncements();

            assertEquals(1, result.size());
            verify(announcementRepository).findAll();
        }

        @Test
        @DisplayName("Get Announcement By ID Service Success")
        void getAnnouncement_Success() throws ResourceNotFoundException {
            when(announcementRepository.findById(1)).thenReturn(Optional.of(announcementEntity));
            when(modelMapper.map(announcementEntity, AnnouncementDto.class)).thenReturn(announcementDto);

            AnnouncementDto result = announcementService.getAnnouncement(1);

            assertEquals(announcementDto.getTitle(), result.getTitle());
            verify(announcementRepository).findById(1);
        }

        @Test
        @DisplayName("Get Announcement By ID Service Not Found")
        void getAnnouncement_NotFound() {
            when(announcementRepository.findById(999)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    announcementService.getAnnouncement(999));

            verify(announcementRepository).findById(999);
        }
    }


    private AnnouncementDto createMockAnnouncementDto() {
        AnnouncementDto dto = new AnnouncementDto();
        dto.setId(1);
        //dto.setWorksite(1);
        dto.setTitle("Test Announcement");
        dto.setMessage("Test Message");
        dto.setStart(new Date());
        dto.setEnd(new Date());
        dto.setApproved(true);
        return dto;
    }

    private AnnouncementEntity createMockAnnouncementEntity() {
        AnnouncementEntity entity = new AnnouncementEntity();
        entity.setId(1);
        WorksiteEntity worksite = new WorksiteEntity();
        worksite.setId(1);
        entity.setWorksite(worksite);
        entity.setTitle("Test Announcement");
        entity.setMessage("Test Message");
        entity.setStart(new Date());
        entity.setEnd(new Date());
        entity.setApproved(true);
        return entity;
    }
}
