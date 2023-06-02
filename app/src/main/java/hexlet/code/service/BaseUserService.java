package hexlet.code.service;

import hexlet.code.dto.UserDetailsDto;
import hexlet.code.dto.UserToSaveDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    public UserDetailsDto update(Long id, UserToSaveDto userToSaveDto) {
        User userToUpdate = toModel(userToSaveDto);
        userToUpdate.setId(id);
        encodePassword(userToUpdate);
        User existentUser = userRepository.findById(id).orElseThrow();
        userToUpdate.setCreatedAt(existentUser.getCreatedAt());
        User updatedUser = userRepository.save(userToUpdate);
        return toDto(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        //TODO in SB3 deleteById no longer throws EmptyDataResult. Do we really need to notify user in this case?
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User doesn't exist");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetailsDto createNew(UserToSaveDto userToSaveDto) {
        User userToCreate = toModel(userToSaveDto);
        encodePassword(userToCreate);
        User createdUser = userRepository.save(toModel(userToSaveDto));
        return toDto(createdUser);
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private User toModel(UserToSaveDto userToSaveDto) {
        User user = new User();
        user.setFirstName(userToSaveDto.getFirstName());
        user.setLastName(userToSaveDto.getLastName());
        user.setEmail(userToSaveDto.getEmail());
        user.setPassword(userToSaveDto.getPassword());
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
