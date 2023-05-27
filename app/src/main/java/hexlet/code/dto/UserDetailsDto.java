package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//TODO find better name
public class UserDetailsDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private Instant createdAt;
}
