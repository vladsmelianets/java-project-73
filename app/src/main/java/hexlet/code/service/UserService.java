package hexlet.code.service;

import hexlet.code.dto.ShowUserDto;
import hexlet.code.dto.SaveUserDto;

import java.util.List;

public interface UserService {
    ShowUserDto createNew(SaveUserDto saveUserDto);

    List<ShowUserDto> getAll();

    ShowUserDto getById(Long id);

    ShowUserDto update(Long id, SaveUserDto saveUserDto);

    void deleteById(Long id);
}
