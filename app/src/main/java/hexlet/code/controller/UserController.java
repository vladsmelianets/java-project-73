package hexlet.code.controller;

import hexlet.code.dto.UserDetailsDto;
import hexlet.code.dto.UserToSaveDto;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{id}")
    public UserDetailsDto update(@PathVariable Long id, @RequestBody @Valid UserToSaveDto userToSaveDto) {
        return userService.update(id, userToSaveDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
