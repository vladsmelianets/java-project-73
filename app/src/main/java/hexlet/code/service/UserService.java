package hexlet.code.service;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;

public interface UserService {

    ShowUserDto createNew(SaveUserDto saveUserDto);
}
