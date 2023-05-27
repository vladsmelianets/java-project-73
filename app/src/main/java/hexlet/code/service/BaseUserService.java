package hexlet.code.service;

import hexlet.code.dto.UserToSaveDto;
import hexlet.code.dto.UserDetailsDto;
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
    public List<UserDetailsDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDetailsDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return toDto(user);
    }

    @Override
    public UserDetailsDto createNew(UserToSaveDto userDto) {
        User createdUser = userRepository.save(toModel(userDto));
        return toDto(createdUser);
    }

    private User toModel(UserToSaveDto userToSaveDto) {
        User user = new User();
        user.setFirstName(userToSaveDto.getFirstName());
        user.setLastName(userToSaveDto.getLastName());
        user.setEmail(userToSaveDto.getEmail());
        user.setPassword(passwordEncoder.encode(userToSaveDto.getPassword()));
        return user;
    }

    private UserDetailsDto toDto(User user) {
        return new UserDetailsDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
