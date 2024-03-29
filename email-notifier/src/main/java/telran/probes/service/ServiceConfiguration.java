package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class ServiceConfiguration {
	@Value("${app.emails.provider.host}")
	String host;
	
	@Value("${app.emails.provider.port}")
	int port;

	@Value("${app.emails.provider.path}")
	String path;

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}