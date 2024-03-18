package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.SensorEmailsProviderService;
import static telran.probes.UrlConstants.*;

import java.util.Arrays;

@RestController
@AllArgsConstructor
@Slf4j
public class SensorEmailsProviderController {
	final SensorEmailsProviderService providerService;

	@GetMapping(SENSOR_EMAILS + "/{" + PROBE_ID + "}")
	String[] getEmailsForSensor(@PathVariable(PROBE_ID) long sensorId) {
		log.debug("--- Debug {} -> getEmailsForSensor for probe {}", getCN(this) , sensorId);
		String[] res = providerService.getSensorEmails(sensorId);
		log.debug("--- Debug {} -> res {}", getCN(this) , Arrays.asList(res));
		return res;

	}

	
}
