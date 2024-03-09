package telran.probes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.dto.Range;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SernsorRangeRepo;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
	final SernsorRangeRepo rangeRepo;

	@Override
	public Range getSensorRange(long sensorId) {
		RangeDoc rd = rangeRepo.findById(sensorId).orElseThrow(() -> new NotFoundException("wrong ID"));
		log.debug("--- Debug SensorRangeProviderServiceImpl -> RangeDoc: {} has been found", rd);
		return rd.build();
	}

	@Override
	public RangeDoc addSensorRange(long id, double minValue, double maxValue) {
		var res = rangeRepo.insert(new RangeDoc(id, minValue, maxValue));
		log.debug("--- Debug SensorRangeProviderServiceImpl -> RangeDoc: {} has been added", res);
		return res;
	}

}
