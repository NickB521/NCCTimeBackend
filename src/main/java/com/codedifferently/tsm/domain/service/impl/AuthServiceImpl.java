package com.codedifferently.tsm.domain.service.impl;

import com.codedifferently.tsm.domain.model.dto.AuthRegisterDto;
import com.codedifferently.tsm.domain.model.entity.RoleEntity;
import com.codedifferently.tsm.domain.model.entity.UserEntity;
import com.codedifferently.tsm.domain.model.entity.WorksiteEntity;
import com.codedifferently.tsm.domain.repository.RoleRepository;
import com.codedifferently.tsm.domain.repository.UserRepository;
import com.codedifferently.tsm.domain.repository.WorksiteRepository;
import com.codedifferently.tsm.domain.service.AuthService;
import com.codedifferently.tsm.exception.ResourceCreationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final WorksiteRepository worksiteRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           WorksiteRepository worksiteRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.worksiteRepository = worksiteRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(AuthRegisterDto registerDto) throws ResourceCreationException {
        Optional<RoleEntity> roleResponse = roleRepository.findByName("EMPLOYEE");
        assert roleResponse.isPresent();

        // Validate AuthRegisterDto
        Optional<WorksiteEntity> worksite = worksiteRepository.findByName(registerDto.getWorksite());
        if (worksite.isEmpty()) {
            throw new ResourceCreationException("Worksite does not exist");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResourceCreationException("Email already exists");
        }

        if (!Pattern.compile("^(.+)@(\\S+)$").matcher(registerDto.getEmail()).matches()) {
            throw new ResourceCreationException("Invalid email");
        }

        if (registerDto.getPhoneNumber() != null) {
            String phoneNumberRegex =
                    "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|" +
                            "[2-9][02-8]1|" +
                            "[2-9][02-8][02-9])\\s*\\)|" +
                            "([2-9]1[02-9]|" +
                            "[2-9][02-8]1|" +
                            "[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|" +
                            "[2-9][02-9]1|" +
                            "[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|" +
                            "x\\.?|" +
                            "ext\\.?|" +
                            "extension)\\s*(\\d+))?$";

            if (!Pattern.compile(phoneNumberRegex).matcher(registerDto.getPhoneNumber()).matches()) {
                throw new ResourceCreationException("Invalid phone number");
            }
        }

        if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
            throw new ResourceCreationException("Invalid password");
        }

        if (registerDto.getFirstName() == null || registerDto.getFirstName().isEmpty()) {
            throw new ResourceCreationException("Invalid first name");
        }

        if (registerDto.getLastName() == null || registerDto.getLastName().isEmpty()) {
            throw new ResourceCreationException("Invalid last name");
        }

        if (registerDto.getDob() == null) {
            throw new ResourceCreationException("Invalid birthdate");
        }

        // Create UserEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(roleResponse.get());
        userEntity.setWorksite(worksite.get());

        userEntity.setEmail(registerDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userEntity.setPhoneNumber(registerDto.getPhoneNumber());
        userEntity.setFirstName(registerDto.getFirstName());
        userEntity.setLastName(registerDto.getLastName());

        userEntity.setDob(registerDto.getDob());
        userEntity.setTitle(registerDto.getJobTitle());

        return userEntity;
    }

}
