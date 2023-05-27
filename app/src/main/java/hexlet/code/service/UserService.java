package hexlet.code.service;

import hexlet.code.dto.UserDetailsDto;
import hexlet.code.dto.UserToSaveDto;

import java.util.List;

public interface UserService {
    UserDetailsDto createNew(UserToSaveDto userToSaveDto);

    List<UserDetailsDto> getAll();

    UserDetailsDto getById(Long id);

    UserDetailsDto update(Long id, UserToSaveDto userToSaveDto);

    void deleteById(Long id);
}
