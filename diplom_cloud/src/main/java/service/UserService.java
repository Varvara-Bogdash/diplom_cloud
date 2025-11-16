package service;

import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String email, String password) {
        log.info("Creating new user with username: {}", username);

        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("Attempt to create user with existing username: {}", username);
            throw new RuntimeException("User with this username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Attempt to create user with existing email: {}", email);
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        log.debug("Searching for user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        log.debug("Searching for user by id: {}", id);
        return userRepository.findById(id);
    }
}