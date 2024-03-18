package telran.probes;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.ProbesService;
import telran.probes.dto.ProbeData;

@Configuration
@Slf4j
@RequiredArgsConstructor
@ComponentScan(basePackages = "telran")
public class ProbesAppl {
	final ProbesService probesService;
	private static final long TIMEOUT = 10000;

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(ProbesAppl.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
	}

	@Bean
	Supplier<ProbeData> probesSupplier() {
		return this::probeGeneration;
	}	

	private ProbeData probeGeneration() {
		return getProbeData();
	}

	private ProbeData getProbeData() {
		String producerBindingName = "probesSupplier-out-0";
		ProbeData res = probesService.getRandomProbeData();
		log.debug("--- Debug ProbesAppl -> Probe data: {} has been sent to: {}", res, producerBindingName);
		return res;
	}
}
