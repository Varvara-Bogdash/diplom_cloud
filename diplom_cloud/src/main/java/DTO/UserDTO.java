package DTO;

import entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private static Logger log;
    private Long id;
    private String username;
    private String email;
    private String password;

    public static UserDTO fromEntity(User user) {
        log.debug("Converting User entity to DTO for user: {}", user.getUsername());
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null // Не возвращаем пароль в DTO
        );
    }

    public static User toEntity(UserDTO userDTO) {
        log.debug("Converting User DTO to entity for user: {}", userDTO.getUsername());
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}