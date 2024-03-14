package telran.probes;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.service.AvgReducerService;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AvgReducerAppl {

	@Value("${app.avg.reducer.producer.binding.name}")
	private String producerBindingName;

	final AvgReducerService reducerService;
	final StreamBridge streamBridge;

	public static void main(String[] args) {
		SpringApplication.run(AvgReducerAppl.class, args);

	}

	@Bean
	Consumer<ProbeData> avgReducerConsumer() {
		return this::probeDataReducing;
	}

	private void probeDataReducing(ProbeData probeData) {
		log.trace("--- Trace AvgReducerAppl -> received probe: {}", probeData);

		long id = probeData.id();
		Double avgValue = reducerService.getAvgValue(probeData);
		log.debug("--- AvgReducerAppl -> AvgValue for sensor ID {} is {}", id, avgValue);

		if (avgValue != null) {
			ProbeData data = new ProbeData(id, avgValue, System.currentTimeMillis());
			streamBridge.send(producerBindingName, data);
			log.debug("--- AvgReducerAppl -> Send AvgValue: {}, to: {}", data, producerBindingName);
		} else {
			log.trace("--- AvgReducerAppl -> No avg value for sensor {}", id);
		}

	}
}