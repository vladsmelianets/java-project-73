package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
