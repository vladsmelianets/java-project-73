package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
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
@Sql("classpath:data.sql")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value(value = "${base-url}")
    private String baseUrl;

    @Test
    void shouldRespondAllUsersJsonWhenGetUsers() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 200/Ok").isEqualTo(200);
        assertThat(response.getContentType()).as("response body should be json")
                .isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).as("response body should contain all users")
                .contains("1", "John", "Smith", "smith@email.com")
                .contains("2", "Jack", "Doe", "doe@email.com");
    }

    @Test
    void shouldRespondUserJsonWhenGetExistentUserById() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 200/Ok").isEqualTo(200);
        assertThat(response.getContentType()).as("response body should be json")
                .isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).as("response body should contain only requested user")
                .contains("1", "John", "Smith", "smith@email.com")
                .doesNotContain("Jack", "Doe", "doe@email.com");
    }

    @Test
    void shouldRespondStatus404WhenGetNonExistentUserById() throws Exception {
        mockMvc.perform(get(baseUrl + "/users/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUserWhenPostValidUserJson() throws Exception {
        final File jsonFile = new ClassPathResource("valid_user.json").getFile();
        final String userToCreate = Files.readString(jsonFile.toPath());
        int usersCountBeforeRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToCreate))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 200/Ok").isEqualTo(200);
        assertThat(response.getContentType()).as("response body should be json")
                .isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).as("response should contain new user")
                .contains(TestData.VALID_FIRSTNAME, TestData.VALID_LASTNAME, TestData.VALID_EMAIL);

        int usersCountAfterRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        assertThat(usersCountAfterRequest).as("valid user should be added to database")
                .isEqualTo(usersCountBeforeRequest + TestData.ONE);
    }

    @Test
    void shouldRespondStatus422WhenPostNonValidUserJson() throws Exception {
        final File jsonFile = new ClassPathResource("non_valid_user.json").getFile();
        final String userToCreate = Files.readString(jsonFile.toPath());
        int usersCountBeforeRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToCreate))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 422").isEqualTo(422);

        int usersCountAfterRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        assertThat(usersCountAfterRequest).as("non-valid user should not be added to database")
                .isEqualTo(usersCountBeforeRequest);
    }

    interface TestData {

        void suppressCheckstyle(); //TODO figure out how to suppress this f***ing checkstyle rule
        int ONE = 1;
        String VALID_FIRSTNAME = "valid firstname";
        String VALID_LASTNAME = "valid lastname";
        String VALID_EMAIL = "valid@email.com";
    }
}
