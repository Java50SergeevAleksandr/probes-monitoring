package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static telran.probes.UrlConstants.*;

import telran.exceptions.NotFoundException;
import telran.probes.dto.Range;
import telran.probes.service.SensorRangeProviderService;

@WebMvcTest
class SensorRangeProviderControllerTest {

	private static final long ID_1 = 1;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	SensorRangeProviderService providerService;

	@Test
	void getRangeForSensor_normalFlow_success() throws Exception {
		Range expected = new Range(100, 200);
		when(providerService.getSensorRange(ID_1)).thenReturn(expected);
		String expectedJSON = mapper.writeValueAsString(expected);
		String response = mockMvc.perform(get("http://localhost:8080/" + SENSOR_RANGE + "1")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertEquals(expectedJSON, response);
	}

	@Test
	void getRangeForSensor_notExists_exception() throws Exception {
		when(providerService.getSensorRange(ID_1)).thenThrow(new NotFoundException("wrong ID"));
		mockMvc.perform(get("http://localhost:8080/" + SENSOR_RANGE + "1")).andExpect(status().isNotFound());
	}

}
