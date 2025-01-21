package com.codedifferently.tsm.domain.service.impl;

import com.codedifferently.tsm.domain.model.dto.HolidayDto;
import com.codedifferently.tsm.domain.model.entity.HolidayEntity;
import com.codedifferently.tsm.domain.repository.HolidayRepository;
import com.codedifferently.tsm.domain.service.HolidayService;
import com.codedifferently.tsm.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HolidayServiceImpl(HolidayRepository holidayRepository, ModelMapper modelMapper) {
        this.holidayRepository = holidayRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<HolidayDto> getAllHolidays() {
        List<HolidayEntity> holidayEntities = holidayRepository.findAll();

        return holidayEntities.stream()
                .map(this::mapHoliday)
                .collect(Collectors.toList());
    }

    @Override
    public HolidayDto getHoliday(Integer id)
            throws ResourceNotFoundException {
        Optional<HolidayEntity> holiday = holidayRepository.findById(id);
        if (holiday.isEmpty()) throw new ResourceNotFoundException("Holiday not found");

        return mapHoliday(holiday.get());
    }

    private HolidayDto mapHoliday(HolidayEntity holidayEntity) {
        return modelMapper.map(holidayEntity, HolidayDto.class);
    }
}