package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;


import telran.probes.dto.ProbeData;
import telran.probes.service.AvgReducerService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgReducerControllerTest {
	private static final long SENSOR_ID_AVG = 1;
	private static final long SENSOR_ID_NO_AVG = 2;

	private static final double VALUE = 100;
	private static final double AVG_VALUE = 150;

	private static final long DIFF_TIMESTAMP = 2000;

	@Autowired
	InputDestination producer;

	@Autowired
	OutputDestination consumer;

	@MockBean
	AvgReducerService reducerService;

	ObjectMapper mapper = new ObjectMapper();

	ProbeData probeNoAvgValue = new ProbeData(SENSOR_ID_NO_AVG, VALUE, 0);
	ProbeData probeAvgValue = new ProbeData(SENSOR_ID_AVG, VALUE, 0);

	@BeforeEach
	void setUpServiceMock() {
		when(reducerService.getAvgValue(probeNoAvgValue)).thenReturn(null);
		when(reducerService.getAvgValue(probeAvgValue)).thenReturn(AVG_VALUE);
	}

	private String consumerBindingName = "avgReducerConsumer-in-0";
	
	@Value("${app.avg.reducer.producer.binding.name}")
	String producerBindingName;

	@Test
	void noAvgValue_success() {
		producer.send(new GenericMessage<ProbeData>(probeNoAvgValue), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNull(message);
	}

	@Test
	void avgValue_success() throws Exception {
		producer.send(new GenericMessage<ProbeData>(probeAvgValue), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNotNull(message);

		ProbeData avgData = mapper.readValue(message.getPayload(), ProbeData.class);
		ProbeData expected = new ProbeData(SENSOR_ID_AVG, AVG_VALUE, System.currentTimeMillis());

		assertEquals(expected.id(), avgData.id());
		assertEquals(expected.value(), avgData.value());
		assertTrue(Math.abs(expected.timestamp() - avgData.timestamp()) < DIFF_TIMESTAMP);

	}
}
