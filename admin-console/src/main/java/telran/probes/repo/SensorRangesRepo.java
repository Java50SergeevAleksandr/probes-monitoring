package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.RangeDoc;

public interface SensorRangesRepo extends MongoRepository<RangeDoc, Long>{

}
