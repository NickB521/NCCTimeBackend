package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.HolidayDto;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import java.util.List;


public interface HolidayService {

    List<HolidayDto> getAllHolidays();
    HolidayDto getHoliday(Integer id) throws ResourceNotFoundException;

}