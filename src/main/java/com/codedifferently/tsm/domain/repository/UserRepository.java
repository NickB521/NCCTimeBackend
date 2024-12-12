package com.codedifferently.tsm.domain.repository;

import com.codedifferently.tsm.domain.model.entity.UserEntity;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
