package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorEmails;
import telran.probes.dto.SensorRange;
import telran.probes.dto.SensorUpdateData;
import telran.probes.exceptions.SensorIllegalStateException;
import telran.probes.exceptions.SensorNotFoundException;
import telran.probes.model.EmailsDoc;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SensorEmailsRepo;
import telran.probes.repo.SensorRangesRepo;
import static telran.probes.exceptionMessages.ExceptionMessages.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminConsoleServiceImpl implements AdminConsoleService {
	final SensorEmailsRepo emailsRepo;
	final SensorRangesRepo rangesRepo;
	final MongoTemplate mongoTemplate;
	final StreamBridge streamBridge;
	
	@Value("${app.update.data.binding.name}")
	String bindingName;
	
	String collectionNameRanges = "sensor_ranges";
	String collectionNameMails = "sensor_emails";
	
	
	FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

	@Override
	public SensorRange addSensorRange(SensorRange sensorRange) {
		long id = sensorRange.id();
		try {
			mongoTemplate.insert(new RangeDoc(sensorRange));
		} catch (DuplicateKeyException e) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor range for a given sensor ID {}, already exists", id);
			throw new SensorIllegalStateException(id, collectionNameRanges);
		}
		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor range: {} has been added", sensorRange);
		return sensorRange;
	}

	@Override
	public SensorRange updateSensorRange(SensorRange sensorRange) {
		long id = sensorRange.id();
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("range", sensorRange.range());

		RangeDoc rangeDoc = mongoTemplate.findAndModify(query, update, options, RangeDoc.class);

		if (rangeDoc == null) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor with id: {} not found", id);
			throw new SensorNotFoundException();
		}

		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor range {} has been updated for sensor with id: {}",
				sensorRange, id);
		
		SensorUpdateData updateData = new SensorUpdateData(id, sensorRange.range(), null);
		streamBridge.send(bindingName, updateData);
		log.debug("--- Debug AdminConsoleServiceImpl -> update data {} have been sent to binding name {}", updateData, bindingName);
		
		return new SensorRange(id, rangeDoc.getRange());
	}

	@Override
	public SensorEmails addSensorEmails(SensorEmails sensorEmails) {
		long id = sensorEmails.id();
		try {
			mongoTemplate.insert(new EmailsDoc(sensorEmails));
		} catch (DuplicateKeyException e) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor emails for a given sensor ID {}, already exists",
					id);
			throw new SensorIllegalStateException(id, collectionNameMails);
		}
		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor emails: {} has been added", sensorEmails);
		return sensorEmails;
	}

	@Override
	public SensorEmails updateSensorEmails(SensorEmails sensorEmails) {
		long id = sensorEmails.id();
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("mails", sensorEmails.mails());

		EmailsDoc emailsDoc = mongoTemplate.findAndModify(query, update, options, EmailsDoc.class);

		if (emailsDoc == null) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor with id: {} not found", id);
			throw new SensorNotFoundException();
		}

		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor emails: {} has been updated for sensor with id: {}",
				sensorEmails, id);
		
		SensorUpdateData updateData = new SensorUpdateData(id, null, sensorEmails.mails());
		streamBridge.send(bindingName, updateData);
		log.debug("--- Debug AdminConsoleServiceImpl -> update data {} have been sent to binding name {}", updateData, bindingName);
		
		return new SensorEmails(id, emailsDoc.getMails());
	}

}
