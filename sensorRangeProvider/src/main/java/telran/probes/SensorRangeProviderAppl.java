package telran.probes;

import org.springframework.boot.SpringApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "telran")
public class SensorRangeProviderAppl {

	public static void main(String[] args) {
		SpringApplication.run(SensorRangeProviderAppl.class, args);

	}

}