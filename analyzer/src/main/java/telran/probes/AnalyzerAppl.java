package telran.probes;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;

import telran.probes.service.RangeProviderClientService;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class AnalyzerAppl {
	@Value("${app.analyzer.producer.binding.name}")
	String producerBindingName;
	final RangeProviderClientService clientService;
	final StreamBridge streamBridge;

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args);

	}

	@Bean
	Consumer<SensorUpdateData> updateRangeConsumer() {
		return SensorUpdateData -> clientService.updateProcessing(SensorUpdateData);
	}

	@Bean
	Consumer<ProbeData> analyzerConsumer() {
		return probeData -> probeDataAnalyzing(probeData);
	}

	private void probeDataAnalyzing(ProbeData probeData) {
		log.trace("--- Trace AnalyzerAppl -> received probe: {}", probeData);
		long id = probeData.id();
		double value = probeData.value();
		Range range = clientService.getRange(id);
		log.debug("--- Debug AnalyzerAppl -> Range for sensor ID {} is {}", id, range);

		double border = Double.NaN;

		if (value < range.minValue()) {
			border = range.minValue();
		} else if (value > range.maxValue()) {
			border = range.maxValue();
		}

		if (!Double.isNaN(border)) {
			double deviation = value - border;
			DeviationData data = new DeviationData(id, deviation, value, probeData.timestamp());
			streamBridge.send(producerBindingName, data);
			log.debug("--- Debug AnalyzerAppl -> Deviation detected {}, sent to {}", data, producerBindingName);
		}

	}

}