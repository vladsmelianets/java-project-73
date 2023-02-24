package hexlet.code;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AppApplicationTests {

    @Test
    void contextLoads(ApplicationContext ctx) {
        Assertions.assertThat(ctx).isNotNull();
    }
}
