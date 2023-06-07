package hexlet.code.service;

import hexlet.code.dto.UserSaveDto;
import hexlet.code.dto.UserShowDto;

import java.util.List;

public interface UserService {
    UserShowDto createNew(UserSaveDto userSaveDto);

    List<UserShowDto> getAll();

    UserShowDto getById(Long id);

    UserShowDto update(Long id, UserSaveDto userSaveDto);

    void deleteById(Long id);
}
