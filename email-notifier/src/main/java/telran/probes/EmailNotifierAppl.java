package telran.probes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.DeviationData;

import telran.probes.service.EmailsProviderClientService;

@Slf4j
@SpringBootApplication
public class EmailNotifierAppl {

	@Value("${app.emails.notifier.subject:deviation of sensor }")
	private String subject;

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	EmailsProviderClientService providerService;

	public static void main(String[] args) {
		SpringApplication.run(EmailNotifierAppl.class, args);

	}

	@Bean
	Consumer<DeviationData> emailNotifierConsumer() {
		return this::sendingMail;
	}

	void sendingMail(DeviationData deviation) {
		log.debug("--- EmailNotifierAppl -> Received deviation data: {}", deviation);
		long sensorId = deviation.id();
		String[] emails = providerService.getMails(sensorId);
		log.debug("--- EmailNotifierAppl -> Received mail addresses are:{}", Arrays.deepToString(emails));
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(emails);
		smm.setText(getText(deviation));
		smm.setSubject(getSubject(sensorId));
		mailSender.send(smm);
		log.debug("--- EmailNotifierAppl -> mail has been sent");

	}

	private String getSubject(long sensorId) {
		String res = subject + sensorId;
		log.debug("--- EmailNotifierAppl -> Subject: {} ", res);
		return res;
	}

	private String getText(DeviationData deviationData) {
		String text = String.format("sensor %d has value %f with deviation %f at %s", deviationData.id(),
				deviationData.value(), deviationData.deviation(), getDateTime(deviationData.timestamp()));
		log.debug("--- EmailNotifierAppl -> Text: {} ", text);
		return text;
	}

	private LocalDateTime getDateTime(long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
