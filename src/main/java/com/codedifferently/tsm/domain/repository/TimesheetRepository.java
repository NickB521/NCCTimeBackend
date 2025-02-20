package com.codedifferently.tsm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codedifferently.tsm.domain.model.entity.TimesheetEntity;

public interface TimesheetRepository extends JpaRepository<TimesheetEntity, Integer>{
    
}
