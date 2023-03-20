package hexlet.code.service;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class BaseUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ShowUserDto createNew(SaveUserDto userDto) {
        User createdUser = userRepository.save(dtoToModel(userDto));
        return modelToDto(createdUser);
    }

    private User dtoToModel(SaveUserDto saveUserDto) {
        User user = new User();
        user.setFirstName(saveUserDto.getFirstName());
        user.setLastName(saveUserDto.getLastName());
        user.setEmail(saveUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(saveUserDto.getPassword()));
        return user;
    }

    private ShowUserDto modelToDto(User user) {
        return new ShowUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
