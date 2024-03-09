package telran.probes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.model.EmailsDoc;
import telran.probes.repo.SensorEmailsRepo;

@Service
@Slf4j
@Transactional(readOnly = true)
@AllArgsConstructor
public class SensorEmailsProviderServiceImpl implements SensorEmailsProviderService {
	final SensorEmailsRepo emailsRepo;

	@Override
	public String[] getSensorEmails(long sensorId) {
		EmailsDoc res = emailsRepo.findById(sensorId).orElseThrow(() -> new IllegalArgumentException("wrong ID"));
		log.debug("--- Debug SensorEmailsProviderServiceImpl -> EmailsDoc: {} has been found", res);
		return res.getMails();
	}

}
