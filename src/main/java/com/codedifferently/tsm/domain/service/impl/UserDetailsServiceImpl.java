package com.codedifferently.tsm.domain.service.impl;

import com.codedifferently.tsm.domain.model.entity.RoleEntity;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        return new User(user.getEmail(), user.getPassword(), getUserRoles(user.getRole()));
    }

    private Collection<GrantedAuthority> getUserRoles(RoleEntity role) {
        // Convert RoleEntity into list of strings
        List<RoleEntity> roles = Collections.singletonList(role);

        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

}
