package service;
import DTO.UserDTO;
import entity.User;
import enums.Role;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import repository.UserRepository;
import unit.MapperUtils;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private MapperUtils mapperUtils;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        userDTO = UserDTO.builder()
                .login("testLogin@test.ru")
                .password("testPassword")
                .build();
        user = User.builder()
                .id(1L)
                .login("testLogin@test.ru")
                .password("testPassword")
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();
    }

    @Test
    public void test_registerUser() {
        Mockito.when(mapperUtils.toUserEntity(userDTO)).thenReturn(user);
        Mockito.when(userRepository.findUserByLogin(user.getLogin())).thenReturn(Optional.empty());

        registrationService.registerUser(userDTO);

        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin("testLogin@test.ru");
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void test_getUser() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        registrationService.getUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void test_deleteUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        registrationService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}