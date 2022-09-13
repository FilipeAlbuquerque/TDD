package com.tdd.TDD.service;

import com.tdd.TDD.dto.UserDto;
import com.tdd.TDD.model.User;
import com.tdd.TDD.repository.UserRepository;
import com.tdd.TDD.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.xml.bind.ValidationException;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto register(UserDto userDto) {
        logger.info("registering user....");
        UserUtil.validateEmail(userDto.getEmail());
        UserUtil.validateFirstName(userDto.getFirstName());
        UserUtil.validateLastName(userDto.getLastName());
        User user = User.builder().build();
        user = userRepository.save(user);
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).phoneNumber(user.getPhoneNumber()).profilePicture(user.getProfilePicture())
                .build();
    }

    @Override
    public UserDto getProfile(String userId) throws ValidationException, ChangeSetPersister.NotFoundException {
        logger.info("getting user profile....");
        if (userId == null) {
            throw new ValidationException("userId cannot be empty");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        User user = userOpt.get();
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).phoneNumber(user.getPhoneNumber()).profilePicture(user.getProfilePicture())
                .build();
    }

    @Override
    public UserDto update(UserDto userDto) throws ValidationException, ChangeSetPersister.NotFoundException {
        logger.info("updating user....");
        if (userDto.getId() == null) {
            throw new ValidationException("userId cannot be empty");
        }
        Optional<User> userOpt = userRepository.findById(userDto.getId());
        if (userOpt.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        User user;
        user = User.builder().firstName(userDto.getFirstName()).lastName(userDto.getLastName())
                .email(userDto.getEmail()).phoneNumber(userDto.getPhoneNumber())
                .profilePicture(userDto.getProfilePicture()).build();
        user = userRepository.save(user);
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).phoneNumber(user.getPhoneNumber()).profilePicture(user.getProfilePicture())
                .build();
    }

    @Override
    public void delete(String userId) throws ValidationException, ChangeSetPersister.NotFoundException {
        logger.info("deleting user....");
        if (userId == null) {
            throw new ValidationException("userId cannot be empty");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        User user;
        user = User.builder().deleted(true).build();
        userRepository.save(user);
    }
}
