package hexlet.code.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.AuthUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JwtProcessor jwtProcessor;

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager,
            final RequestMatcher loginRequest,
            final JwtProcessor jwtProcessor) {
        super(authenticationManager);
        super.setRequiresAuthenticationRequestMatcher(loginRequest);
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
            final HttpServletResponse response) throws AuthenticationException {
        log.debug("TRYING TO LOGIN");
        final AuthUserDto loginData = getLoginData(request);
        log.debug("LOGIN DATA EXTRACTED: {}", loginData);
        final var authRequest = new UsernamePasswordAuthenticationToken(
                loginData.getEmail(),
                loginData.getPassword()
        );
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private AuthUserDto getLoginData(final HttpServletRequest request) throws AuthenticationException {
        try {
            final String json = request.getReader()
                    .lines()
                    .collect(Collectors.joining());
            return MAPPER.readValue(json, AuthUserDto.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Can't extract login data from request");
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain, final Authentication authResult) throws IOException {
        final UserDetails user = (UserDetails) authResult.getPrincipal();
        final String token = jwtProcessor.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, user.getUsername()));

        response.getWriter().println(token);
    }
}