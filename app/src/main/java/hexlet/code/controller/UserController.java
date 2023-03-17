package hexlet.code.controller;

import hexlet.code.dto.SaveUserDto;
import hexlet.code.dto.ShowUserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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

    UserRepository userRepository;

    @GetMapping
    public List<ShowUserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(ShowUserDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ShowUserDto getById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return new ShowUserDto(user);
    }

    @PostMapping
    public ShowUserDto create(@RequestBody @Valid SaveUserDto saveUserDto) {
        //TODO throw 422 if non-valid dto
        User createdUser = userRepository.save(saveUserDto.toModel());
        return new ShowUserDto(createdUser);
    }
}
