package hexlet.code.service;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public final class BaseUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ShowUserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ShowUserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return toDto(user);
    }

    @Override
    public ShowUserDto createNew(SaveUserDto userDto) {
        User createdUser = userRepository.save(toModel(userDto));
        return toDto(createdUser);
    }

    private User toModel(SaveUserDto saveUserDto) {
        User user = new User();
        user.setFirstName(saveUserDto.getFirstName());
        user.setLastName(saveUserDto.getLastName());
        user.setEmail(saveUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(saveUserDto.getPassword()));
        return user;
    }

    private ShowUserDto toDto(User user) {
        return new ShowUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
