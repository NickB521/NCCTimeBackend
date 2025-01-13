package com.codedifferently.tsm.domain.repository;

import com.codedifferently.tsm.domain.model.entity.HolidayEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, Integer> {

    Optional<HolidayEntity> findByTitle(String title);
    List<HolidayEntity> findAllByType(String type);
    List<HolidayEntity> findAllByDateRange(Date dateRange);

}
