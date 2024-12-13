package com.codedifferently.tsm.domain.repository;

import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorksiteRepository extends JpaRepository<WorksiteEntity, Integer> {

    Optional<WorksiteEntity> findByName(String name);

}
