package edu.eci.patricia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration"
})
class EeveevoidingResponsibilitiesCampusEventsApplicationTests {


	@Test
	void contextLoads() {
	}

}
