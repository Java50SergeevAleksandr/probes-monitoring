package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import telran.probes.dto.Range;

@Document(collection = "range")
@Getter
public class RangeDoc {
	@Id
	long id;

	double minValue;
	double maxValue;

	public Range build() {
		return new Range(minValue, maxValue);
	}

	public RangeDoc(long id, double minValue, double maxValue) {

		this.id = id;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
}
