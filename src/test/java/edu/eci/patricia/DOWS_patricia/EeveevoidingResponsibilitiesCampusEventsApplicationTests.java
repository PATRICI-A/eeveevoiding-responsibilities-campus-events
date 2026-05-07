package edu.eci.patricia.DOWS_patricia;

import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration"
})
class EeveevoidingResponsibilitiesCampusEventsApplicationTests {

	@MockBean
	EventoRepository eventoRepository;

	@Test
	void contextLoads() {
	}

}
