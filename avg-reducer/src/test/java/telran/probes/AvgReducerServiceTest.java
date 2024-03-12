package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import telran.probes.dto.ProbeData;
import telran.probes.model.ProbesList;
import telran.probes.repo.ProbesListRepo;
import telran.probes.service.AvgReducerService;

@SpringBootTest
class AvgReducerServiceTest {
	private static final Double VALUE1 = 100.0;
	private static final Double VALUE2 = 200.0;
	private static final Double MED_VALUE = 150.0;
	@Autowired
	AvgReducerService reducerService;

	@MockBean
	ProbesListRepo probesListRepo;

	HashMap<Long, ProbesList> redisMockMap = new HashMap<>();
	private ProbeData probeData1 = new ProbeData(123, VALUE1, 0);
	private ProbeData probeData2 = new ProbeData(123, VALUE2, 0);

	@BeforeEach
	void mockSetUp() {
		when(probesListRepo.findById(any(Long.class))).then(new Answer<Optional<ProbesList>>() {

			@Override
			public Optional<ProbesList> answer(InvocationOnMock invocation) throws Throwable {
				Long sensorId = invocation.getArgument(0);
				ProbesList probesList = redisMockMap.get(sensorId);
				return probesList == null ? Optional.ofNullable(null) : Optional.of(probesList);
			}
		});
		when(probesListRepo.save(any(ProbesList.class))).then(new Answer<ProbesList>() {

			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				ProbesList probesList = invocation.getArgument(0);
				redisMockMap.put(probesList.getSensorId(), probesList);
				return probesList;
			}
		});
	}

	@Test
	void getAvgValue_normalFlow_success() {
		Double res = reducerService.getAvgValue(probeData1);
		assertNull(res);
		res = reducerService.getAvgValue(probeData2);
		assertNotNull(res);
		assertEquals(MED_VALUE, res);
		res = reducerService.getAvgValue(probeData1);
		assertNull(res);
		res = reducerService.getAvgValue(probeData2);
		assertNotNull(res);
		assertEquals(MED_VALUE, res);

	}

}