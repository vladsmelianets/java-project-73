package hexlet.code.service;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    ShowUserDto createNew(SaveUserDto saveUserDto);

    List<ShowUserDto> getAll();

    ShowUserDto getById(@PathVariable Long id);
}
