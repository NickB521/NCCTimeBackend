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
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public HolidayDto getHoliday(Integer id) throws ResourceNotFoundException {
        HolidayEntity holidayEntity = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with id: " + id));
        return mapToDto(holidayEntity);
    }


    private HolidayDto mapToDto(HolidayEntity holidayEntity) {
        return modelMapper.map(holidayEntity, HolidayDto.class);
    }

}