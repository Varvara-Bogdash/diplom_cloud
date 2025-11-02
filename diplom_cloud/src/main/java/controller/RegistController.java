package controller;

import DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;;

import jakarta.validation.Valid;
import service.RegistrationService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RegistController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<UserDTO>(registrationService.registerUser(userDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return new ResponseEntity<UserDTO>(registrationService.getUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        registrationService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
