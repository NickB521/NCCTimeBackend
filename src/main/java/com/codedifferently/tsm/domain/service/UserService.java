package com.codedifferently.tsm.domain.service;

import com.codedifferently.tsm.domain.model.dto.UserDto;
import com.codedifferently.tsm.exception.PermissionDeniedException;
import com.codedifferently.tsm.exception.ResourceNotFoundException;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(Collection<GrantedAuthority> authorities)
            throws PermissionDeniedException;

    UserDto getUser(Integer id, Collection<GrantedAuthority> authorities)
            throws PermissionDeniedException, ResourceNotFoundException;

    List<UserDto> getWorksiteUsers(Integer id, Collection<GrantedAuthority> authorities)
            throws PermissionDeniedException, ResourceNotFoundException;

}
