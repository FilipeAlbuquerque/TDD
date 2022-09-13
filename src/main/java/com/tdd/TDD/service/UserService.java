package com.tdd.TDD.service;

import com.tdd.TDD.dto.UserDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

@Service
public interface UserService {

        UserDto register(UserDto userDto);

        UserDto getProfile(String userId) throws ValidationException, ChangeSetPersister.NotFoundException;

        UserDto update(UserDto userDto) throws ValidationException, ChangeSetPersister.NotFoundException;

        void delete(String userId) throws ValidationException, ChangeSetPersister.NotFoundException;

}
