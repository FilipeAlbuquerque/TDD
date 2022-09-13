package com.tdd.TDD.service;

import com.tdd.TDD.dto.UserDto;
import com.tdd.TDD.model.User;
import com.tdd.TDD.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    //InjectMocks annotation marks the field where injection should be performed.
    @InjectMocks
    UserService userService = new UserServiceImpl();

    //Mock annotation allows to mock the field
    @Mock
    UserRepository userRepository;
    User USER_RECORD_1;
    User USER_RECORD_2;

    @BeforeEach
    void init() {
        USER_RECORD_1 = User.builder().id("aebv").firstName("Miguel").lastName("Albuquerque")
                .email("miguel@gmail.com").phoneNumber("11222333").deleted(false).build();
        USER_RECORD_2 = User.builder().id("aebvs").firstName("Filipe").lastName("Albuquerque")
                .email("filipe@gmail.com").phoneNumber("22334455").deleted(false).build();
    }

    @Test
    void shouldRegisterNewUser_IfUserNewRegistrationDataIsValid() {
        UserDto newUser = UserDto.builder().firstName("Miguel").lastName("Albuquerque").email("miguel@gmail.com")
                .phoneNumber("11222333").build();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(USER_RECORD_1);
        UserDto savedUser = userService.register(newUser);
        Assertions.assertNotNull(savedUser);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals(savedUser.getFirstName(), "Miguel");
        Assertions.assertEquals(savedUser.getLastName(), "Albuquerque");

    }

    @Test
    void shouldReturnUserProfile_IfUserExistsInDatabase() throws ValidationException, ChangeSetPersister.NotFoundException {

        Optional<User> existingUser = Optional.of(USER_RECORD_1);
        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(existingUser);
        String userId = "aebv";
        UserDto userProfile = userService.getProfile(userId);
        Assertions.assertNotNull(userProfile);
        Assertions.assertEquals(userProfile.getFirstName(), "Miguel");
        Assertions.assertEquals(userProfile.getLastName(), "Albuquerque");
    }

    @Test
    void shouldDeleteUser_IfUserExistsAndHasNotDeletedBeforeInDatabase() throws ValidationException, ChangeSetPersister.NotFoundException {
        Optional<User> existingUser = Optional.of(USER_RECORD_1);
        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(existingUser);
        String userId = "aebv";
        userService.delete(userId);
    }

    @Test
    void shouldUpdateRegisteredUser_IfUserExistsInDatabase() throws ValidationException, ChangeSetPersister.NotFoundException {
        UserDto updateUser = UserDto.builder().id("aebvs").firstName("Peter").lastName("Parker")
                .email("peter@gmail.com").phoneNumber("22334455").build();
        Optional<User> updatedResult = Optional.of(USER_RECORD_2);
        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(updatedResult);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(USER_RECORD_2);
        UserDto updatedUser = userService.update(updateUser);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updatedUser.getFirstName(), "Filipe");
        Assertions.assertEquals(updatedUser.getLastName(), "Albuquerque");
    }

    @Test
    void shouldThrowNotFoundException_IfUserIsNotFoundInDatabaseDuringUpdate() {
        UserDto registeredUser = UserDto.builder().id("feetetws").firstName("Ana").lastName("Albuquerque")
                .email("ana@gmail.com").phoneNumber("11222333").build();
        Optional<User> emptyResult = Optional.empty();
        Mockito.when(userRepository.findById(Mockito.any(String.class))).thenReturn(emptyResult);
        Exception exception = Assertions.assertThrows(ChangeSetPersister.NotFoundException.class,
                () -> userService.update(registeredUser));
        Assertions.assertNull(exception.getMessage());
    }
}
