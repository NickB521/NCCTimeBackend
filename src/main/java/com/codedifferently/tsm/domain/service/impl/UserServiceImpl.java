package com.codedifferently.tsm.domain.service.impl;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.repository.UserRepository;
import com.codedifferently.tsm.domain.service.UserService;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Integer id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) throw new ResourceNotFoundException("User not found");

        return mapUser(user.get());
    }

    private UserDto mapUser(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

}
