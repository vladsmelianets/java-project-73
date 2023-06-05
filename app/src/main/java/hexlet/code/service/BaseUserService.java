package hexlet.code.service;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;
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
    public ShowUserDto update(Long id, SaveUserDto saveUserDto) {
        User userToUpdate = toModel(saveUserDto);
        userToUpdate.setId(id);
        encodeUserPassword(userToUpdate);
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
    public ShowUserDto createNew(SaveUserDto saveUserDto) {
        User userToCreate = toModel(saveUserDto);
        encodeUserPassword(userToCreate);
        User createdUser = userRepository.save(userToCreate);
        return toDto(createdUser);
    }

    private void encodeUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private User toModel(SaveUserDto saveUserDto) {
        User user = new User();
        user.setFirstName(saveUserDto.getFirstName());
        user.setLastName(saveUserDto.getLastName());
        user.setEmail(saveUserDto.getEmail());
        user.setPassword(saveUserDto.getPassword());
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
