package telran.probes.service;

import telran.probes.dto.SensorEmails;
import telran.probes.dto.SensorRange;

public interface AdminConsoleService {
	SensorRange addSensorRange(SensorRange sensorRange);
	SensorRange updateSensorRange(SensorRange sensorRange);
	SensorEmails addSensorEmails(SensorEmails sensorEmails);
	SensorEmails updateSensorEmails(SensorEmails sensorEmails);
}
