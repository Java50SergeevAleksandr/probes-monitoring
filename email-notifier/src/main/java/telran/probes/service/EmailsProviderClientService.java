package telran.probes.service;

public interface EmailsProviderClientService {
	String SERVICE_EMAIL = "${ACC_NAME}";

	String[] getMails(long sensorId);
}
