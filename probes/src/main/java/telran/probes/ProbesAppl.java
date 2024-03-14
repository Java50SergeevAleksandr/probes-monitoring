package telran.probes;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.ProbesService;
import telran.probes.dto.ProbeData;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ProbesAppl {

	final StreamBridge streamBridge;
	final ProbesService probesService;

	private String producerBindingName = "probesSupplier-out-0";

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(ProbesAppl.class, args);
	}

	@Bean
	Supplier<ProbeData> probesSupplier() {
		return this::probeGeneration;
	}

	private ProbeData probeGeneration() {
		return getProbeData();
	}

	private ProbeData getProbeData() {
		ProbeData res = probesService.getRandomProbeData();
		streamBridge.send(producerBindingName, res);
		log.debug("--- Debug ProbesAppl -> Probe data: {} has been sent to: {}", res, producerBindingName);
		return res;
	}
}
