package controller;

import DTO.UserDTO;
import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        log.info("Received request to create user: {}", userDTO.getUsername());
        try {
            User user = userService.createUser(
                    userDTO.getUsername(),
                    userDTO.getEmail(),
                    userDTO.getPassword()
            );

            UserDTO responseDTO = UserDTO.fromEntity(user);
            log.info("User created successfully: {}", user.getUsername());
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            log.error("Error creating user {}: {}", userDTO.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
