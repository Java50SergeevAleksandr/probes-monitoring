package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.probes.dto.SensorEmails;

@Document(collection = "emails")
@Getter
@ToString
@NoArgsConstructor
public class EmailsDoc {
	@Id
	long id;

	String[] mails;

	public EmailsDoc(SensorEmails sensorEmails) {
		this.id = sensorEmails.id();
		this.mails = sensorEmails.mails();
	}
}
