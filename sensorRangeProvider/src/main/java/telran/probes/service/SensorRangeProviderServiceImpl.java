package telran.probes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.exceptions.*;
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
		RangeDoc rd = rangeRepo.findById(sensorId).orElseThrow(() -> new SensorNotFoundException());
		log.debug("--- Debug SensorRangeProviderServiceImpl -> RangeDoc: {} has been found", rd);
		return rd.getRange();
	}
	

}
