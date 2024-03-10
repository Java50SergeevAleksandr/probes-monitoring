package telran.probes.service;

import telran.probes.dto.Range;
import telran.probes.dto.SensorUpdateData;

public interface RangeProviderClientService {
	double MIN_DEFAULT_VALUE = -100;
	double MAX_DEFAULT_VALUE = 100;

	Range getRange(long sensorId);

	void updateProcessing(SensorUpdateData data);
}
