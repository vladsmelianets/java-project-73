package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
//TODO remove addFilters for security tests
//TODO consider to implement something of these for security tests:
//https://spring.io/blog/2014/05/23/preview-spring-security-test-web-security
@AutoConfigureMockMvc(addFilters = false)
@Sql("classpath:data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

//TODO enable for security tests
    //    @Autowired
//    private WebApplicationContext context;

    //TODO disable autowired for security tests
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value(value = "${base-url}")
    private String baseUrl;

    private static String validUserJson;
    private static String nonValidUserJson;
    private static Map<String, String> validUser;
    private static Map<String, String> nonValidUser;

    @BeforeAll
    static void setUpTestData() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final File validUserFile = new ClassPathResource("valid_user.json").getFile();
        final File nonValidUserFile = new ClassPathResource("non_valid_user.json").getFile();

        validUserJson = Files.readString(validUserFile.toPath());
        nonValidUserJson = Files.readString(nonValidUserFile.toPath());
        validUser = mapper.readValue(validUserJson, Map.class);
        nonValidUser = mapper.readValue(nonValidUserJson, Map.class);
    }

//TODO enable for security tests
//    @BeforeEach
//    public void setupMockMvc() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }

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
        int usersCountBeforeRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 200/Ok").isEqualTo(200);
        assertThat(response.getContentType()).as("response body should be json")
                .isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).as("response should contain new user")
                .contains(validUser.get("firstName"), validUser.get("lastName"), validUser.get("email"));

        int usersCountAfterRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        assertThat(usersCountAfterRequest).as("valid user should be added to database")
                .isGreaterThan(usersCountBeforeRequest);
    }

    @Test
    void shouldRespondStatus422WhenPostNonValidUserJson() throws Exception {
        int usersCountBeforeRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonValidUserJson))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 422").isEqualTo(422);

        int usersCountAfterRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        assertThat(usersCountAfterRequest).as("non-valid user should not be added to database")
                .isEqualTo(usersCountBeforeRequest);
    }

    @Test
    void shouldUpdateUserWhenPutValidExistentUserJson() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 200/Ok").isEqualTo(200);
        assertThat(response.getContentType()).as("response body should be json")
                .isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).as("response should contain updated user")
                .contains(validUser.get("firstName"), validUser.get("lastName"), validUser.get("email"));

        int updatedUserCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "first_name = '" + validUser.get("firstName") + "'");
        assertThat(updatedUserCount).as("user should be updated")
                .isOne();
    }

    @Test
    void shouldRespondStatus404WhenPutNonExistentUserToUpdate() throws Exception {
        mockMvc.perform(put(baseUrl + "/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondStatus422WhenPutNonValidUserJson() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonValidUserJson))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 422").isEqualTo(422);

        int updatedUserCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "first_name = '" + nonValidUser.get("firstName") + "'");
        assertThat(updatedUserCount).as("user should be updated")
                .isZero();
    }

    @Test
    void shouldDeleteUserWhenDeleteByExistentId() throws Exception {
        int usersCountBeforeRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/users/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 200/Ok").isEqualTo(200);

        int usersCountAfterRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        assertThat(usersCountAfterRequest).as("existent user should be deleted from database")
                .isLessThan(usersCountBeforeRequest);

        int deletedUserCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users", "id = 1");
        assertThat(deletedUserCount).as("user with id 1 should be deleted from database")
                .isZero();
    }

    @Test
    void shouldNotDeleteUserWhenDeleteByNonExistentId() throws Exception {
        int usersCountBeforeRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/users/100"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).as("response status should be 404").isEqualTo(404);

        int usersCountAfterRequest = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        assertThat(usersCountAfterRequest).as("nothing should be deleted from database")
                .isEqualTo(usersCountBeforeRequest);
    }
}
