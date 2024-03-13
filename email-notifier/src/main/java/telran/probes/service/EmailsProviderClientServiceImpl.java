package telran.probes.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorUpdateData;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailsProviderClientServiceImpl implements EmailsProviderClientService {
	final RestTemplate restTemplate;
	final ServiceConfiguration serviceConfiguration;

	HashMap<Long, String[]> cashe = new LinkedHashMap<Long, String[]>(16, 0.74f, true);

	@Bean
	Consumer<SensorUpdateData> updateEmailsConsumer() {
		return this::updateProcessing;
	}

	public void updateProcessing(SensorUpdateData data) {
		String[] emails = data.emails();
		log.debug("--- Debug EmailsProviderClientServiceImpl -> updateProcessing , SensorUpdateData has emails: {}",
				Arrays.deepToString(emails));

		if (emails != null) {
			long sensorId = data.id();
			var res = cashe.computeIfPresent(sensorId, (id, v) -> emails);
			log.debug(
					"--- Debug EmailsProviderClientServiceImpl -> updateProcessing,  for id: {} , new value in cashe map: {}",
					sensorId, res);
		}

	}

	@Override
	public String[] getMails(long sensorId) {
		String[] emails = cashe.computeIfAbsent(sensorId, id -> serviceRequest(sensorId));
		log.debug("--- Debug EmailsProviderClientServiceImpl -> emails value in cashe map: {}",
				Arrays.deepToString(emails));

		if (emails == null) {
			emails = new String[] { SERVICE_EMAIL };
			log.warn("--- Debug EmailsProviderClientServiceImpl -> set default mails: {}", Arrays.deepToString(emails));
		}

		return emails;
	}

	private String[] serviceRequest(long sensorId) {
		String[] emails = null;
		ResponseEntity<?> responseEntity;
		try {
			responseEntity = restTemplate.exchange(getUrl(sensorId), HttpMethod.GET, null, String[].class);
			if (responseEntity.getStatusCode().isError()) {
				throw new Exception(responseEntity.getBody().toString());
			}
			emails = (String[]) responseEntity.getBody();
			log.debug("--- Debug EmailsProviderClientServiceImpl -> serviceRequest return mails: {}",
					Arrays.deepToString(emails));
		} catch (Exception e) {
			log.error("--- Debug EmailsProviderClientServiceImpl -> error at service request: {}", e.getMessage());
		}
		return emails;

	}

	private String getUrl(long sensorId) {
		String url = String.format("http://%s:%d%s%d", serviceConfiguration.getHost(), serviceConfiguration.getPort(),
				serviceConfiguration.getPath(), sensorId);
		log.debug("--- Debug EmailsProviderClientServiceImpl -> url created is {}", url);
		return url;
	}
}
