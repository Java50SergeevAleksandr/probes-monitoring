package telran.probes.service;

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

	FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

	@Override
	public SensorRange addSensorRange(SensorRange sensorRange) {
		long id = sensorRange.id();
		try {
			mongoTemplate.insert(new RangeDoc(sensorRange));
		} catch (DuplicateKeyException e) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor range for a given sensor ID {}, already exists", id);
			throw new SensorIllegalStateException(SENSOR_RANGE_ALREADY_EXISTS);
		}
		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor range: {} has been added", sensorRange);
		return sensorRange;
	}

	@Override
	public SensorRange updateSensorRange(SensorRange sensorRange) {
		long id = sensorRange.id();
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.push("maxValue", sensorRange.range().maxValue());
		update.push("minValue", sensorRange.range().minValue());
		RangeDoc rangeDoc = mongoTemplate.findAndModify(query, update, options, RangeDoc.class);

		if (rangeDoc == null) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor with id: {} not found", id);
			throw new SensorNotFoundException();
		}

		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor range {} has been updated for sensor with id: {}",
				sensorRange, id);
		return new SensorRange(id, rangeDoc.build());
	}

	@Override
	public SensorEmails addSensorEmails(SensorEmails sensorEmails) {
		long id = sensorEmails.id();
		try {
			mongoTemplate.insert(new EmailsDoc(sensorEmails));
		} catch (DuplicateKeyException e) {
			log.error("--- Debug AdminConsoleServiceImpl -> Sensor emails for a given sensor ID {}, already exists",
					id);
			throw new SensorIllegalStateException(SENSOR_EMAILS_ALREADY_EXISTS);
		}
		log.debug("--- Debug AdminConsoleServiceImpl -> Sensor emails: {} has been added", sensorEmails);
		return sensorEmails;
	}

	@Override
	public SensorEmails updateSensorEmails(SensorEmails sensorEmails) {
		// TODO Auto-generated method stub
		return null;
	}

}
