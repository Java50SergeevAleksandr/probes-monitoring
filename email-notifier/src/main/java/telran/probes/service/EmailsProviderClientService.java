package telran.probes.service;

public interface EmailsProviderClientService {
	String SERVICE_EMAIL = "admin-sensors@gmail.com";

	String[] getMails(long sensorId);
}
