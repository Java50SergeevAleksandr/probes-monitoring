package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.ToString;

@Document(collection = "emails")
@Getter
@ToString
public class EmailsDoc {
	@Id
	long id;

	String[] mails;

	public EmailsDoc(long id, String[] mails) {
		this.id = id;
		this.mails = mails;
	}
}
