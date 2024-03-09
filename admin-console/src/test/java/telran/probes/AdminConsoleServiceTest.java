package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.Range;
import telran.probes.dto.SensorRange;
import telran.probes.exceptions.SensorIllegalStateException;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SensorEmailsRepo;
import telran.probes.repo.SensorRangesRepo;
import telran.probes.service.AdminConsoleServiceImpl;
import static telran.probes.exceptionMessages.ExceptionMessages.*;

@SpringBootTest
@RequiredArgsConstructor
class AdminConsoleServiceTest {
	private static final long ID_1 = 1;
	private static final long ID_2 = 2;

	@Autowired
	SensorEmailsRepo emailsRepo;

	@Autowired
	SensorRangesRepo rangesRepo;

	@Autowired
	AdminConsoleServiceImpl service;

	Range range = new Range(100, 200);
	SensorRange sensorRange = new SensorRange(ID_1, range);

	@Test
	void addSensorRange_normalFlow_success() {
		assertEquals(sensorRange, service.addSensorRange(sensorRange));
		RangeDoc rangeDoc = rangesRepo.findById(ID_1)
				.orElseThrow(() -> new SensorIllegalStateException(SENSOR_RANGE_ALREADY_EXISTS));
		assertEquals(range, rangeDoc.build());
		assertEquals(ID_1, rangeDoc.getId());

	}

	@Test
	void addSensorRange_alreadyExists_exception() {
		assertThrowsExactly(SensorIllegalStateException.class, () -> service.addSensorRange(sensorRange),
				SENSOR_RANGE_ALREADY_EXISTS);
	}

	@Test	
	void updateSensorRange_normalFlow_success() {
		Range rangeNew = new Range(10, 20);
		SensorRange sensorRangeNew = new SensorRange(ID_1, rangeNew);
		assertEquals(sensorRangeNew, service.updateSensorRange(sensorRangeNew));
		assertEquals(rangeNew, rangesRepo.findById(ID_1).orElseThrow().build());
	}
}
