package com.codedifferently.tsm.domain.repository;

import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    List<UserEntity> findByWorksite(WorksiteEntity worksite);

}
