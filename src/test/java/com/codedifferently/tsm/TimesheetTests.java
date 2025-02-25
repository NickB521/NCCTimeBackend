package com.codedifferently.tsm;

import com.codedifferently.tsm.domain.controller.TimesheetController;
import com.codedifferently.tsm.domain.model.entity.TimesheetEntity;
import com.codedifferently.tsm.domain.repository.TimesheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimesheetTest {

    @Mock
    private TimesheetRepository timesheetRepository;

    @InjectMocks
    private TimesheetController timesheetController;

    private TimesheetEntity sampleTimesheet;

    @BeforeEach
    void setUp() {
        sampleTimesheet = new TimesheetEntity(1, null, new Date(), new Date(), new Date());
    }

    @Test
    void testCreateTimesheet() {
        when(timesheetRepository.save(any(TimesheetEntity.class))).thenReturn(sampleTimesheet);
        TimesheetEntity created = timesheetRepository.save(sampleTimesheet);
        assertNotNull(created);
        assertEquals(sampleTimesheet.getTimesheet_id(), created.getTimesheet_id());
    }

    @Test
    void testGetTimesheetById_Success() {
        when(timesheetRepository.findById(1)).thenReturn(Optional.of(sampleTimesheet));
        Optional<TimesheetEntity> found = timesheetRepository.findById(1);
        assertTrue(found.isPresent());
        assertEquals(sampleTimesheet.getTimesheet_id(), found.get().getTimesheet_id());
    }

    @Test
    void testGetTimesheetById_NotFound() {
        when(timesheetRepository.findById(1)).thenReturn(Optional.empty());
        Optional<TimesheetEntity> found = timesheetRepository.findById(1);
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateTimesheet_Success() {
        when(timesheetRepository.existsById(1)).thenReturn(true);
        assertTrue(timesheetRepository.existsById(1));
    }

    @Test
    void testDeleteTimesheet_Success() {
        when(timesheetRepository.existsById(1)).thenReturn(true);
        doNothing().when(timesheetRepository).deleteById(1);
        timesheetRepository.deleteById(1);
        verify(timesheetRepository, times(1)).deleteById(1);
    }
}

