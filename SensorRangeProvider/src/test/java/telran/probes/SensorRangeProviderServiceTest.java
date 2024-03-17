package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.probes.exceptions.*;
import telran.probes.dto.Range;
import telran.probes.dto.SensorRange;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SernsorRangeRepo;
import telran.probes.service.SensorRangeProviderService;

@SpringBootTest
class SensorRangeProviderServiceTest {
	@Autowired
	SernsorRangeRepo rangeRepo;

	@Autowired
	SensorRangeProviderService providerService;

	@Test
	void getSensorRange_normalFlow_success() {
		RangeDoc rangeDoc = new RangeDoc(new SensorRange(0, new Range(100, 200)));
		rangeRepo.insert(rangeDoc);
		assertEquals(new Range(100, 200), providerService.getSensorRange(0));
	}

	@Test
	void getSensorRange_notExist_exception() throws Exception {
		assertThrowsExactly(SensorNotFoundException.class, () -> providerService.getSensorRange(1));
	}
}
