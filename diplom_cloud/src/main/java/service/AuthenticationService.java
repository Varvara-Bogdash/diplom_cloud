package service;
import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password) {
        log.info("Authentication attempt for user: {}", username);

        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            log.warn("Authentication failed - user not found: {}", username);
            return false;
        }

        boolean isAuthenticated = passwordEncoder.matches(password, user.get().getPassword());
        if (isAuthenticated) {
            log.info("User authenticated successfully: {}", username);
        } else {
            log.warn("Authentication failed - invalid password for user: {}", username);
        }

        return isAuthenticated;
    }
}
}