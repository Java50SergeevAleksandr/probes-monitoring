package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.EmailsDoc;

public interface SensorEmailsRepo extends MongoRepository<EmailsDoc, Long> {

}
