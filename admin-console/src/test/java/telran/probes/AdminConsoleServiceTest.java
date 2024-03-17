package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.Range;
import telran.probes.dto.SensorEmails;
import telran.probes.dto.SensorRange;
import telran.probes.exceptions.SensorIllegalStateException;
import telran.probes.exceptions.SensorNotFoundException;
import telran.probes.model.EmailsDoc;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SensorEmailsRepo;
import telran.probes.repo.SensorRangesRepo;
import telran.probes.service.AdminConsoleServiceImpl;
import static telran.probes.exceptionMessages.ExceptionMessages.*;

@SpringBootTest
@RequiredArgsConstructor
class AdminConsoleServiceTest {
	private static final long ID_1 = 1;	

	@Autowired
	SensorEmailsRepo emailsRepo;

	@Autowired
	SensorRangesRepo rangesRepo;

	@Autowired
	AdminConsoleServiceImpl service;

	Range range = new Range(100, 200);
	String[] mails = { "m1", "m2" };

	SensorRange sensorRange = new SensorRange(ID_1, range);
	SensorEmails sensorEmails = new SensorEmails(ID_1, mails);

	@BeforeEach
	void setUp() {
		rangesRepo.deleteAll();
		emailsRepo.deleteAll();
	}

	@Test
	void addSensorRange_normalFlow_success() {
		assertEquals(sensorRange, service.addSensorRange(sensorRange));
		RangeDoc rangeDoc = rangesRepo.findById(ID_1)
				.orElseThrow(() -> new SensorIllegalStateException(0, null));
		assertEquals(range, rangeDoc.getRange());
		assertEquals(ID_1, rangeDoc.getId());
	}

	@Test
	void addSensorRange_alreadyExists_exception() {
		service.addSensorRange(sensorRange);
		assertThrowsExactly(SensorIllegalStateException.class, () -> service.addSensorRange(sensorRange),
				SENSOR_RANGE_ALREADY_EXISTS);
	}

	@Test
	void updateSensorRange_normalFlow_success() {
		service.addSensorRange(sensorRange);
		Range rangeNew = new Range(10, 20);
		SensorRange sensorRangeNew = new SensorRange(ID_1, rangeNew);
		assertEquals(sensorRangeNew, service.updateSensorRange(sensorRangeNew));
		assertEquals(rangeNew, rangesRepo.findById(ID_1).orElseThrow().getRange());
	}

	@Test
	void updateSensorRange_notFound_exception() {
		assertThrowsExactly(SensorNotFoundException.class, () -> service.updateSensorRange(sensorRange));
	}

	@Test
	void updateSensorEmails_normalFlow_success() {
		service.addSensorEmails(sensorEmails);
		String[] mailsNew = { "newm1", "newm2" };
		SensorEmails sensorEmailsNew = new SensorEmails(ID_1, mailsNew);
		assertEquals(sensorEmailsNew, service.updateSensorEmails(sensorEmailsNew));
		assertArrayEquals(mailsNew, emailsRepo.findById(ID_1).orElseThrow().getMails());
	}

	@Test
	void updateSensorEmails_notFound_exception() {
		assertThrowsExactly(SensorNotFoundException.class, () -> service.updateSensorEmails(sensorEmails));
	}

	@Test
	void addSensorEmails_alreadyExists_exception() {
		service.addSensorEmails(sensorEmails);
		assertThrowsExactly(SensorIllegalStateException.class, () -> service.addSensorEmails(sensorEmails),
				SENSOR_EMAILS_ALREADY_EXISTS);
	}

	@Test
	void addSensorEmails_normalFlow_success() {
		assertEquals(sensorEmails, service.addSensorEmails(sensorEmails));
		EmailsDoc emailDoc = emailsRepo.findById(ID_1)
				.orElseThrow(() -> new SensorIllegalStateException(0, null));
		assertArrayEquals(mails, emailDoc.getMails());
		assertEquals(ID_1, emailDoc.getId());
	}
}
