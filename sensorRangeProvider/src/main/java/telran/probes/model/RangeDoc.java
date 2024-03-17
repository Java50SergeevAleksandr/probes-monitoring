package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.probes.dto.Range;
import telran.probes.dto.SensorRange;

@Document(collection = "ranges")
@Getter
@NoArgsConstructor
@ToString
public class RangeDoc {
	@Id
	long id;

	Range range;

	public RangeDoc(SensorRange sensorRange) {
		this.id = sensorRange.id();
		this.range = sensorRange.range();
	}
}
