package hexlet.code.service;

import hexlet.code.dto.UserToSaveDto;
import hexlet.code.dto.UserDetailsDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    UserDetailsDto createNew(UserToSaveDto userToSaveDto);

    List<UserDetailsDto> getAll();

    UserDetailsDto getById(@PathVariable Long id);
}
