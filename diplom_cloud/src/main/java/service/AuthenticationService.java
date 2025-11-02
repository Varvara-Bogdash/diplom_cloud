package service;
import DTO.UserDTO;
import entity.User;
import exception.InvalidInputDataException;
import exception.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import model.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.UserRepository;
import security.JwtProvider;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Token login(UserDTO userDTO) {
        User user = findUserInStorage(userDTO.getLogin());
        if (isEquals(userDTO, user)) {
            String accessToken = jwtProvider.generateAccessToken(user);
            return new Token(accessToken);
        } else {
            throw new InvalidInputDataException("Wrong password", 0);
        }
    }

    public String logout(String authToken, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = findUserInStorage(auth.getName());
        SecurityContextLogoutHandler securityContextLogoutHandler =
                new SecurityContextLogoutHandler();
        if (user != null) {
            securityContextLogoutHandler.logout(request, response, auth);
            jwtProvider.addAuthTokenInBlackList(authToken);
            return user.getLogin();
        }
        return null;
    }

    private User findUserInStorage(String login) {
        return userRepository.findUserByLogin(login).orElseThrow(() ->
                new UserNotFoundException("User not found by login", 0));
    }

    private boolean isEquals(@NonNull User userDTO, User userFromDatabase) {
        return passwordEncoder.matches(userDTO.getPassword(), userFromDatabase.getPassword());
    }
}