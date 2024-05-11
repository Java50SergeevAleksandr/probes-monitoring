package telran.probes;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.DeviationData;

@SpringBootApplication
@Slf4j
public class AwsSnsNotifier {

	@Value("${ARN}")
	private String arn;

	@Value("${app.emails.notifier.subject}")
	private String subjectName;

	@Value("${app.aws.sns.region:us-east-1}")
	String awsRegion;

	AmazonSNS client;

	public static void main(String[] args) {
		SpringApplication.run(AwsSnsNotifier.class, args);

	}

	@Bean
	Consumer<DeviationData> emailNotifierConsumer() {
		return this::sendingMail;
	}

	void sendingMail(DeviationData deviation) {
		log.debug("---> Received deviation data: {}", deviation);

		long sensorId = deviation.id();
		String topicArn = getTopicArn(sensorId);
		log.debug("---> topic is: {}", topicArn);

		String subject = getSubject(sensorId);
		log.debug("---> subject is: {}", subject);

		client.publish(topicArn, deviation.toString(), subject);
		log.debug("---> notification has been sent");
	}

	private String getSubject(long sensorId) {
		return subjectName + sensorId;
	}

	private String getTopicArn(long sensorId) {
		return arn + ":sensor-" + sensorId;
	}

	@PostConstruct
	void setSnsClient() {
		client = AmazonSNSClient.builder().withRegion(awsRegion).build();
	}

}
