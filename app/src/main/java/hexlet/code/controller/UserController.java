package hexlet.code.controller;

import hexlet.code.dto.UserToSaveDto;
import hexlet.code.dto.UserDetailsDto;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + "/users")
public final class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDetailsDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDetailsDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserDetailsDto create(@RequestBody @Valid UserToSaveDto userToSaveDto) {
        return userService.createNew(userToSaveDto);
    }
}
