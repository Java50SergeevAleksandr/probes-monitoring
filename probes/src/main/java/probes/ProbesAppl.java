package probes;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;

@SpringBootApplication
@Slf4j
public class ProbesAppl {
	@Value("${app.probes.sleep.timeout:10000}")
	private static final long TIMEOUT = 0;

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
		// TODO Auto-generated method stub
		return null;
	}
}
