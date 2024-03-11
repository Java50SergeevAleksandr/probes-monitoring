package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorUpdateData;
import telran.probes.service.EmailsProviderClientService;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class EmailNotifierAppl {
	final EmailsProviderClientService service;

	public static void main(String[] args) {
		SpringApplication.run(EmailNotifierAppl.class, args);

	}

	@Bean
	Consumer<SensorUpdateData> updateEmailsConsumer() {
		return service::updateProcessing;
	}
}
