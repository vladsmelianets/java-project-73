package hexlet.code.controller;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class UserController {

    private final UserService userService;

    private static final String ACCOUNT_OWNER = """
                @userRepository.findById(#id).get().getEmail() == authentication.getName()
            """;

    @GetMapping
    public List<ShowUserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ShowUserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public ShowUserDto create(@RequestBody @Valid SaveUserDto saveUserDto) {
        return userService.createNew(saveUserDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(ACCOUNT_OWNER)
    public ShowUserDto update(@PathVariable Long id, @RequestBody @Valid SaveUserDto saveUserDto) {
        return userService.update(id, saveUserDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(ACCOUNT_OWNER)
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
