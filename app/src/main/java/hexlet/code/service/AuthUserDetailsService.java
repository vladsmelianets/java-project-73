package hexlet.code.service;

import hexlet.code.repository.UserRepository;
import hexlet.code.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("VALIDATING USER {}", username);
        org.springframework.security.core.userdetails.User user = userRepository.findByEmail(username)
                .map(x -> new org.springframework.security.core.userdetails.User(
                        x.getEmail(),
                        x.getPassword(),
                        SecurityConfig.DEFAULT_AUTHORITIES)
                )
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with 'email': " + username));
        log.debug("USER DETAILS EXTRACTED: {}", user);
        return user;
    }
}
