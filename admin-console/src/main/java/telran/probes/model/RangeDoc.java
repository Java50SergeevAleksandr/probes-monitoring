package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.probes.dto.Range;
import telran.probes.dto.SensorRange;

@Document(collection = "ranges")
@Getter
@NoArgsConstructor
public class RangeDoc {
	@Id
	long id;

	double minValue;
	double maxValue;

	public Range build() {
		return new Range(minValue, maxValue);
	}

	public RangeDoc(SensorRange sensorRange) {

		this.id = sensorRange.id();
		this.minValue = sensorRange.range().minValue();
		this.maxValue = sensorRange.range().maxValue();
	}
}
