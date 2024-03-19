package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.SensorEmailsProviderService;
import static telran.probes.UrlConstants.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SensorEmailsProviderController {
	final SensorEmailsProviderService providerService;
	String className = this.getClass().getSimpleName();
	 
	@GetMapping("${app.emails.provider.path}" + "{" + PROBE_ID + "}")
	String[] getEmailsForSensor(@PathVariable(PROBE_ID) long sensorId) {
		log.debug("--- Debug {} -> getEmailsForSensor for probe {}", className , sensorId);
		String[] res = providerService.getSensorEmails(sensorId);
		log.debug("--- Debug {} -> res {}", className , Arrays.asList(res));
		return res;

	}

	
}
