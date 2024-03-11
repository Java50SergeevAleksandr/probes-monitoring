package telran.probes.service;

import telran.probes.dto.SensorUpdateData;

public interface EmailsProviderClientService {
	String SERVICE_EMAIL = "admin-sensors@gmail.com";
	
	String[] getMails(long sensorId);

	void updateProcessing(SensorUpdateData data);
}
