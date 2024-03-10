package telran.probes.service;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.Range;

@Service
@RequiredArgsConstructor
@Slf4j
public class RangeProviderClientServiceImpl implements RangeProviderClientService {
	final RestTemplate restTemplate;
	final ServiceConfiguration serviceConfiguration;

	@Override
	public Range getRange(long sensorId) {
		HashMap<Long, Range> cashe = new LinkedHashMap<Long, Range>(16, 0.74f, true);
		Range range = cashe.get(sensorId);
		log.debug("--- Debug RangeProviderClientServiceImpl -> range value in cashe map: {}", range);

		if (range == null) {
			range = serviceRequest(sensorId);

			if (range != null) {
				cashe.put(sensorId, range);
				log.debug("--- Debug RangeProviderClientServiceImpl -> put in cashe map: {}", range);
			} else {
				range = new Range(MIN_DEFAULT_VALUE, MAX_DEFAULT_VALUE);
				log.warn("--- Debug RangeProviderClientServiceImpl -> set default range value: {}", range);
			}
		}

		return range;
	}

	private Range serviceRequest(long sensorId) {
		Range range = null;
		ResponseEntity<?> responseEntity;
		try {
			responseEntity = restTemplate.exchange(getUrl(sensorId), HttpMethod.GET, null, Range.class);
			if (responseEntity.getStatusCode().isError()) {
				throw new Exception(responseEntity.getBody().toString());
			}
			range = (Range) responseEntity.getBody();
			log.debug("--- Debug RangeProviderClientServiceImpl -> serviceRequest return range value: {}", range);
		} catch (Exception e) {
			log.error("--- Debug RangeProviderClientServiceImpl -> error at service request: {}", e.getMessage());
		}
		return range;

	}

	private String getUrl(long sensorId) {
		String url = String.format("http://%s:%d%s%d", serviceConfiguration.getHost(), serviceConfiguration.getPort(),
				serviceConfiguration.getPath(), sensorId);
		log.debug("--- Debug RangeProviderClientServiceImpl -> url created is {}", url);
		return url;
	}

}