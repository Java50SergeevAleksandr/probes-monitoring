package telran.probes.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.Range;
import telran.probes.dto.SensorUpdateData;

@Service
@RequiredArgsConstructor
@Slf4j
public class RangeProviderClientServiceImpl implements RangeProviderClientService {
	final RestTemplate restTemplate;
	final ServiceConfiguration serviceConfiguration;

	HashMap<Long, Range> cashe = new LinkedHashMap<Long, Range>(16, 0.74f, true);

	@Override
	public void updateProcessing(SensorUpdateData data) {
		Range[] oldValue = { null };
		Range[] newValue = { null };
		log.debug("--- Debug RangeProviderClientServiceImpl -> updateProcessing , SensorUpdateData has range: {}",
				data.range());

		if (data.range() != null) {
			cashe.computeIfPresent(data.id(), (id, v) -> {
				oldValue[0] = v;
				newValue[0] = data.range();
				return newValue[0];
			});
			log.debug(
					"--- Debug RangeProviderClientServiceImpl -> updateProcessing,  for id: {} , old value: {}, new value in cashe map: {}",
					data.id(), oldValue[0], newValue[0]);
		}

	}

	@Override
	public Range getRange(long sensorId) {
		Range range = cashe.computeIfAbsent(sensorId, id -> serviceRequest(sensorId));
		log.debug("--- Debug RangeProviderClientServiceImpl -> range value in cashe map: {}", range);
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