package telran.probes.service;

public interface EmailsProviderClientService {
	String SERVICE_EMAIL = "${app.emails.provider.default:${ACC_NAME}}";

	String[] getMails(long sensorId);
}
