package telran.probes.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static telran.probes.UrlConstants.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorEmails;
import telran.probes.dto.SensorRange;
import telran.probes.service.AdminConsoleService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminConsoleController {
	final AdminConsoleService service;

	@PostMapping("${app.admin.range.urn}")
	SensorRange addSensorRange(@RequestBody @Valid SensorRange sensorRange) {
		log.debug("--- Debug AdminConsoleServiceImpl -> addSensorRange: received sensorRange: {}", sensorRange);
		return service.addSensorRange(sensorRange);
	}

	@PutMapping("${app.admin.range.urn}")
	SensorRange updateSensorRange(@RequestBody @Valid SensorRange sensorRange) {
		log.debug("--- Debug AdminConsoleServiceImpl -> updateSensorRange: received sensorRange: {}", sensorRange);
		return service.updateSensorRange(sensorRange);
	}

	@PostMapping("${app.admin.emails.urn}")
	SensorEmails addSensorEmails(@RequestBody @Valid SensorEmails sensorEmails) {
		log.debug("--- Debug AdminConsoleServiceImpl -> addSensorEmails: received sensorEmails: {}", sensorEmails);
		return service.addSensorEmails(sensorEmails);
	}

	@PutMapping(SENSOR_EMAILS)
	SensorEmails updateSensorEmails(@RequestBody @Valid SensorEmails sensorEmails) {
		log.debug("--- Debug AdminConsoleServiceImpl -> updateSensorEmails: received sensorEmails: {}", sensorEmails);
		return service.updateSensorEmails(sensorEmails);
	}

}
