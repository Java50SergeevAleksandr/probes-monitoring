package telran.probes.service;

import telran.probes.dto.Range;
import telran.probes.model.RangeDoc;

public interface SensorRangeProviderService {
	Range getSensorRange(long sensorId);
	RangeDoc addSensorRange(long id, double minValue, double maxValue);
}
