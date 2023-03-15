package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc

// TODO find out why script data.sql works only if executed manually
@Sql("classpath:data.sql")
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value(value = "${base-url}")
    private String baseUrl;

    @Test
    void shouldRespondAllUsersJsonWhenGetUsers() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("1", "John", "Smith", "smith@email.com");
        assertThat(response.getContentAsString()).contains("2", "Jack", "Doe", "doe@email.com");
    }

    @Test
    void shouldRespondUserJsonWhenGetExistentUserById() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("1", "John", "Smith", "smith@email.com");
        assertThat(response.getContentAsString()).doesNotContain("Jack", "Doe", "doe@email.com");
    }

    @Test
    void shouldRespondStatus404WhenGetNonExistentUserById() throws Exception {
        mockMvc.perform(get(baseUrl + "/users/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUserWhenPostUserData() throws Exception {
        final File jsonFile = new ClassPathResource("new-test-user.json").getFile();
        final String userToCreate = Files.readString(jsonFile.toPath());

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToCreate))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("new firstname", "new lastname", "new@mail.com");
    }
}
