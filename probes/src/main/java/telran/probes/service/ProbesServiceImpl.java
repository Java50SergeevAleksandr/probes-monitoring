package telran.probes.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.configuration.ProbesConfiguration;
import telran.probes.dto.ProbeData;
import telran.probes.dto.Range;
import telran.probes.model.SensorRangeDoc;
import telran.probes.repo.SensorRangesRepo;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProbesServiceImpl implements ProbesService {
	final SensorRangesRepo sensorRangesRepo;
	final ProbesConfiguration probesConfiguration;

	Map<Long, Range> rangesMap;
	long[] idSet;

	@Override
	public ProbeData getRandomProbeData() {
		long id = getRandomId();
		Range range = rangesMap.get(id);
		return new ProbeData(id,
				getRandomInt(1, 101) <= probesConfiguration.getDeviationPercent() ? getRandomDeviation(range)
						: getRandomNormalValue(range),
				System.currentTimeMillis());
	}

	private double getRandomNormalValue(Range range) {
		return ThreadLocalRandom.current().nextDouble(range.minValue(), range.maxValue());
	}

	private double getRandomDeviation(Range range) {
		return getRandomInt(1, 101) <= probesConfiguration.getNegativeDeviationPercent() ? getLessMin(range.minValue())
				: getGreaterMax(range.maxValue());
	}

	private double getGreaterMax(double maxValue) {
		double res = maxValue + Math.abs(maxValue * probesConfiguration.getDeviationFactor());
		log.debug("--- Debug ProbesServiceImpl -> positive deviation - maxValue: {}, new value: {}", maxValue, res);
		return res;
	}

	private double getLessMin(double minValue) {
		double res = minValue - Math.abs(minValue * probesConfiguration.getDeviationFactor());
		log.debug("--- Debug ProbesServiceImpl -> negative deviation - minValue: {}, new value: {}", minValue, res);
		return res;
	}

	private int getRandomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	private long getRandomId() {
		int index = getRandomInt(0, idSet.length);
		return idSet[index];
	}

	@PostConstruct
	void fillRangesMap() {
		List<SensorRangeDoc> documents = sensorRangesRepo.findAll();

		if (documents.isEmpty()) {
			log.warn("--- Warn ProbesServiceImpl -> Unit test data implied");
			documents = List.of(new SensorRangeDoc(123, new Range(10, 100)),
					new SensorRangeDoc(124, new Range(-10, 10)), new SensorRangeDoc(125, new Range(150, 300)));
		}

		rangesMap = documents.stream().collect(Collectors.toMap(d -> d.getSensorId(), d -> d.getRange()));
		log.trace("--- Trace ProbesServiceImpl -> Map of ranges is {}", rangesMap);

		idSet = rangesMap.keySet().stream().mapToLong(v -> v).toArray();
		log.trace("--- Trace ProbesServiceImpl -> sensor ids are {}", idSet);
	}

}
