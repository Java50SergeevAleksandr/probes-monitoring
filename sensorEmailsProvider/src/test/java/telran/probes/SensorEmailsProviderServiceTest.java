package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.probes.exceptions.*;
import telran.probes.model.EmailsDoc;

import telran.probes.repo.SensorEmailsRepo;
import telran.probes.service.SensorEmailsProviderService;

@SpringBootTest
class SensorEmailsProviderServiceTest {

	@Autowired
	SensorEmailsProviderService emailsService;

	@Autowired
	SensorEmailsRepo emailsRepo;

	@Test
	void getSensorEmails_normalFlow_success() {
		String[] mails = { "m1", "m2" };
		emailsRepo.insert(new EmailsDoc(1, mails));
		assertArrayEquals(mails, emailsService.getSensorEmails(1));
	}

	@Test
	void getSensorEmails_notExist_exception() throws Exception {
		assertThrowsExactly(SensorNotFoundException.class, () -> emailsService.getSensorEmails(0));
	}

}
