package com.wasd.website.service.user;

import com.wasd.website.entity.Role;
import com.wasd.website.entity.User;
import com.wasd.website.model.user.UserRequest;
import com.wasd.website.model.user.UserRole;
import com.wasd.website.repository.UserRepository;
import com.wasd.website.service.role.impl.RoleServiceImpl;
import com.wasd.website.service.user.impl.UserServiceImpl;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {

        User user1 = new User();
        user1.setUsername("user");
        User user2 = new User();
        user2.setUsername("user2");
        List<User> userList = List.of(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);
    }

    @Test
    void create_whenUserExists_throwsEntityExistsException() {
        String username = "user";
        setUpFindByUsernameMethod(username, userRepository.findAll());

        UserRequest createRequest = new UserRequest(username, "123", "123");

        Assertions.assertThrows(EntityExistsException.class, () -> userService.create(createRequest));
    }

    @Test
    void create_whenUserNotExists_saveUser() {
        String username = "newUser";
        setUpFindByUsernameMethod(username, userRepository.findAll());
        UserRequest createRequest = new UserRequest(username, "123", "123");
        Role role = new Role(1L, "ROLE_USER", null);
        Mockito.when(roleService.getUserRole(UserRole.USER)).thenReturn(role);

        userService.create(createRequest);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void updateUsername_whenUsernameExists_throwsEntityExistsException() {
        String username = "user";
        String newName = "user2";
        setUpFindByUsernameMethod(username, userRepository.findAll());
        setUpFindByUsernameMethod(newName, userRepository.findAll());
        UserRequest request = new UserRequest(newName, "", "");

        Assertions.assertThrows(EntityExistsException.class, () -> userService.update(username, request));
    }

    @Test
    void updateUsername_whenUsernameNotExists_changeUsername() {
        String username = "user";
        setUpFindByUsernameMethod(username, userRepository.findAll());
        UserRequest request = new UserRequest("user3", "", "");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());

        userService.update(username, request);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);

    }

    private void setUpFindByUsernameMethod(String username, List<User> users) {
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(users.stream()
                        .filter(user -> user.getUsername().equals(username))
                        .findFirst());
    }
}
