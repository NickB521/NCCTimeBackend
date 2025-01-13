package com.codedifferently.tsm.domain.repository;

import com.codedifferently.tsm.domain.model.entity.AnnouncementEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Integer> {

    Optional<AnnouncementEntity> findByTitleContaining(String title);

    List<AnnouncementEntity> findAllByWorksite(WorksiteEntity worksite);
    List<AnnouncementEntity> findAllByDateRange(Date dateRange);
    List<AnnouncementEntity> findALlByApproved(Boolean approved);

}
