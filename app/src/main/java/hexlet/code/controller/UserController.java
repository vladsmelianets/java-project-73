package hexlet.code.controller;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + "/users")
public final class UserController {

    UserRepository userRepository;

    @GetMapping
    public List<UserDto> getAll() {
//        return userRepository.findAll()
//                .stream()
//                .map(User::toDto)
//                .toList();
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        return new UserDto(user);
    }

    @PostMapping
    public UserDto create(@RequestBody SaveUserDto saveUserDto) {
        User createdUser = userRepository.save(saveUserDto.toModel());
        return new UserDto(createdUser);
    }
}
