package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AvgPopulatorAppl {
	final MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorAppl.class, args);
	}

	@Bean
	Consumer<ProbeData> avgPopulatorConsumer() {
		return this::probeDataPopulation;
	}

	private void probeDataPopulation(ProbeData probeData) {
		log.debug("--- Debug AvgPopulatorAppl -> Received probeData: {}", probeData);
		ProbeDataDoc savedDoc = mongoTemplate.insert(new ProbeDataDoc(probeData));
		log.debug("--- Debug AvgPopulatorAppl -> Document {} has been saved to Database", savedDoc);
	}
}
