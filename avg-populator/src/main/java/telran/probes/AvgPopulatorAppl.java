package telran.probes;

import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgPopulatorAppl {
	final MongoTemplate mongoTemplate;
	AmazonDynamoDB client;
	DynamoDB dynamo;
	Table table;

	@Value("${app.aws.dynamo.table:avg_values}")
	String tableName;

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
		log.debug("--- Debug AvgPopulatorAppl -> Document {} has been saved to MongoDB", savedDoc);

		Map<String, Object> map = getMap(probeData);
		table.putItem(new PutItemSpec().withItem(Item.fromMap(map)));
		log.debug("--- Debug AvgPopulatorAppl -> Document {} has been saved to DynamoDB", map);
	}

	private Map<String, Object> getMap(ProbeData probeData) {
		Map<String, Object> res = new HashMap<>();
		res.put("sensorID", probeData.id());
		res.put("timestamp", probeData.timestamp());
		res.put("value", probeData.value());
		return res;
	}

	@PostConstruct
	void setDynamoDB() {
		client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		dynamo = new DynamoDB(client);
		table = dynamo.getTable(tableName);
	}
}
