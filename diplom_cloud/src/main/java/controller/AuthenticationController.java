package controller;
import config.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthRequest authRequest) {
        log.info("Login attempt for user: {}", authRequest.getUsername());
        boolean isAuthenticated = authService.authenticate(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        if (isAuthenticated) {
            log.info("Login successful for user: {}", authRequest.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Login failed for user: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
}