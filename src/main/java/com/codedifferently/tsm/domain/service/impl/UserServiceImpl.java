package com.codedifferently.tsm.domain.service.impl;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import com.codedifferently.tsm.domain.repository.UserRepository;
import com.codedifferently.tsm.domain.repository.WorksiteRepository;
import com.codedifferently.tsm.domain.service.UserService;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final WorksiteRepository worksiteRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, WorksiteRepository worksiteRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.worksiteRepository = worksiteRepository;
    }

    @Override
    public List<UserDto> getAllUsers(Collection<GrantedAuthority> authorities)
            throws PermissionDeniedException {

        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("EMPLOYEE"))) {
            throw new PermissionDeniedException("Permission denied");
        }

        List<UserEntity> userEntities = userRepository.findAll();

        return userEntities.stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Integer id, Collection<GrantedAuthority> authorities)
            throws PermissionDeniedException, ResourceNotFoundException {

        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("EMPLOYEE"))) {
            throw new PermissionDeniedException("Permission denied");
        }

        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) throw new ResourceNotFoundException("User not found");

        return mapUser(user.get());
    }

    @Override
    public List<UserDto> getWorksiteUsers(Integer id, Collection<GrantedAuthority> authorities)
            throws PermissionDeniedException, ResourceNotFoundException {

        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("EMPLOYEE"))) {
            throw new PermissionDeniedException("Permission denied");
        }

        Optional<WorksiteEntity> worksiteEntity = worksiteRepository.findById(id);
        if (worksiteEntity.isEmpty()) throw new ResourceNotFoundException("Worksite not found");

        List<UserEntity> userEntities = userRepository.findByWorksite(worksiteEntity.get());

        return userEntities.stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    private UserDto mapUser(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

}
