package telran.probes;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;

@Slf4j
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class ProbesTest {

	@Autowired
	OutputDestination consumer;

	@Autowired
	ObjectMapper mapper;

	String bindingName = "probesSupplier-out-0";

	@Value("${app.probes.sleep.timeout:10000}")
	private long TIMEOUT;

	@Test
	@Disabled
	void test() throws InterruptedException {
		for (int i = 0; i < 50; i++) {
			assertNotNull(consumer.receive(1500, bindingName));
		}
	}

	@Test
	void testProbeGenerator() throws InterruptedException, StreamReadException, DatabindException, IOException {
		log.debug("--- Debug ProbesTest -> TIMEOUT is: {} ", TIMEOUT);
		Thread.sleep(TIMEOUT);
		Message<byte[]> message;
		int count = 0;

		do {
			message = consumer.receive(10, bindingName);
			count++;
			ProbeData probeData = null;

			if (message != null) {
				probeData = mapper.readValue(message.getPayload(), ProbeData.class);
			}

			log.debug("--- Debug ProbesTest -> receive Probe data: {} ", probeData);
		} while (message != null);

		log.debug("--- Debug ProbesTest -> number of messages received {} ", count);
	}

}