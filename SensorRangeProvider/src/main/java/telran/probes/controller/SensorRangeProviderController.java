package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static telran.probes.UrlConstants.*;
import telran.probes.dto.Range;
import telran.probes.service.SensorRangeProviderService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SensorRangeProviderController {
	final SensorRangeProviderService providerService;

	@GetMapping(SENSOR_RANGE + "/{" + PROBE_ID + "}")
	Range getRangeForSensor(@PathVariable(PROBE_ID) long sensorId) {
		log.debug("--- Debug SensorRangeProviderController -> getRangeForSensor for probe {}", sensorId);
		var res = providerService.getSensorRange(sensorId);
		log.debug("--- Debug SensorRangeProviderController -> res {}", res);
		return res;
	}
}
