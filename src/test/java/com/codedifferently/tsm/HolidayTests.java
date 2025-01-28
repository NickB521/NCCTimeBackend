package com.codedifferently.tsm;
import com.codedifferently.tsm.domain.controller.HolidayController;
import com.codedifferently.tsm.domain.model.dto.HolidayDto;
import com.codedifferently.tsm.domain.model.entity.HolidayEntity;
import com.codedifferently.tsm.domain.repository.HolidayRepository;
import com.codedifferently.tsm.domain.service.impl.HolidayServiceImpl;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HolidayTests {

    @Nested
    @DisplayName("Holiday Controller Tests")
    class HolidayControllerTests {
        @Mock
        private HolidayServiceImpl holidayService;
        private HolidayController holidayController;
        private HolidayDto holidayDto;

        @BeforeEach
        void setUp() {
            holidayController = new HolidayController(holidayService);
            holidayDto = createMockHoliday(1, "New Year's Day", "Federal Holiday", new Date());
        }

        @Test
        @DisplayName("Get All Holidays - Empty List")
        void getAllHolidays_EmptyList() {
            when(holidayService.getAllHolidays()).thenReturn(new ArrayList<>());

            ResponseEntity<List<HolidayDto>> response = holidayController.getAllHolidays();

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isEmpty());
            verify(holidayService).getAllHolidays();
        }

        @Test
        @DisplayName("Get All Holidays - Multiple Holidays")
        void getAllHolidays_MultipleHolidays() {
            List<HolidayDto> holidays = Arrays.asList(
                    createMockHoliday(1, "New Year's Day", "Federal Holiday", new Date()),
                    createMockHoliday(2, "Christmas Day", "Federal Holiday", new Date()),
                    createMockHoliday(3, "Independence Day", "Federal Holiday", new Date())
            );
            when(holidayService.getAllHolidays()).thenReturn(holidays);

            ResponseEntity<List<HolidayDto>> response = holidayController.getAllHolidays();

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(3, response.getBody().size());
            assertEquals("New Year's Day", response.getBody().get(0).getTitle());
            assertEquals("Christmas Day", response.getBody().get(1).getTitle());
            verify(holidayService).getAllHolidays();
        }

        @Test
        @DisplayName("Get Holiday By ID - Success")
        void getHolidayById_Success() {
            when(holidayService.getHoliday(1)).thenReturn(holidayDto);

            ResponseEntity<HolidayDto> response = holidayController.getHolidayById(1);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(holidayDto.getTitle(), response.getBody().getTitle());
            assertEquals(holidayDto.getType(), response.getBody().getType());
            verify(holidayService).getHoliday(1);
        }

        @Test
        @DisplayName("Get Holiday By ID - Not Found")
        void getHolidayById_NotFound() {
            when(holidayService.getHoliday(999))
                    .thenThrow(new ResourceNotFoundException("Holiday not found"));

            ResponseEntity<HolidayDto> response = holidayController.getHolidayById(999);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            verify(holidayService).getHoliday(999);
        }

        @Test
        @DisplayName("Handle Resource Not Found Exception")
        void handleResourceNotFoundException() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Test exception");
            ResponseEntity<String> response = holidayController.handleResourceNotFoundException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Test exception", response.getBody());
        }
    }

    @Nested
    @DisplayName("Holiday Service Tests")
    class HolidayServiceTests {
        @Mock
        private HolidayRepository holidayRepository;
        @Mock
        private ModelMapper modelMapper;
        @InjectMocks
        private HolidayServiceImpl holidayService;

        private HolidayEntity holidayEntity;
        private HolidayDto holidayDto;

        @BeforeEach
        void setUp() {
            Date holidayDate = new Date();
            holidayEntity = createMockHolidayEntity(1, "New Year's Day", "Federal Holiday", holidayDate);
            holidayDto = createMockHoliday(1, "New Year's Day", "Federal Holiday", holidayDate);
        }

        @Test
        @DisplayName("Get All Holidays Service - Empty Repository")
        void getAllHolidays_EmptyRepository() {
            when(holidayRepository.findAll()).thenReturn(new ArrayList<>());

            List<HolidayDto> result = holidayService.getAllHolidays();

            assertTrue(result.isEmpty());
            verify(holidayRepository).findAll();
        }

        @Test
        @DisplayName("Get All Holidays Service - Multiple Holidays")
        void getAllHolidays_MultipleHolidays() {
            List<HolidayEntity> entities = Arrays.asList(
                    createMockHolidayEntity(1, "New Year's Day", "Federal Holiday", new Date()),
                    createMockHolidayEntity(2, "Christmas Day", "Federal Holiday", new Date())
            );
            when(holidayRepository.findAll()).thenReturn(entities);
            when(modelMapper.map(any(HolidayEntity.class), eq(HolidayDto.class)))
                    .thenReturn(holidayDto);

            List<HolidayDto> result = holidayService.getAllHolidays();

            assertEquals(2, result.size());
            verify(holidayRepository).findAll();
            verify(modelMapper, times(2)).map(any(HolidayEntity.class), eq(HolidayDto.class));
        }

        @Test
        @DisplayName("Get Holiday By ID Service - Success")
        void getHoliday_Success() {
            when(holidayRepository.findById(1)).thenReturn(Optional.of(holidayEntity));
            when(modelMapper.map(holidayEntity, HolidayDto.class)).thenReturn(holidayDto);

            HolidayDto result = holidayService.getHoliday(1);

            assertNotNull(result);
            assertEquals(holidayDto.getId(), result.getId());
            assertEquals(holidayDto.getTitle(), result.getTitle());
            verify(holidayRepository).findById(1);
            verify(modelMapper).map(holidayEntity, HolidayDto.class);
        }

        @Test
        @DisplayName("Get Holiday By ID Service - Not Found")
        void getHoliday_NotFound() {
            when(holidayRepository.findById(999)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    holidayService.getHoliday(999)
            );
            verify(holidayRepository).findById(999);
        }
    }

    private static HolidayDto createMockHoliday(Integer id, String title, String type, Date dateRange) {
        HolidayDto dto = new HolidayDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setType(type);
        dto.setDateRange(dateRange);
        return dto;
    }

    private static HolidayEntity createMockHolidayEntity(Integer id, String title, String type, Date dateRange) {
        HolidayEntity entity = new HolidayEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setType(type);
        entity.setDateRange(dateRange);
        return entity;
    }

}
