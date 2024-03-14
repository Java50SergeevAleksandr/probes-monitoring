package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.UrlConstants.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.Range;
import telran.probes.dto.SensorEmails;
import telran.probes.dto.SensorRange;
import telran.probes.service.AdminConsoleService;

@WebMvcTest
class AdminConsoleControllerTest {

	@MockBean
	AdminConsoleService adminConsoleService;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	private static final long ID_1 = 1;

	Range range = new Range(100, 200);
	String[] mails = { "m1", "m2" };

	SensorRange sensorRange = new SensorRange(ID_1, range);
	SensorEmails sensorEmails = new SensorEmails(ID_1, mails);

	@Test
	void addSensorRange_normalFlow_success() throws Exception {
		when(adminConsoleService.addSensorRange(sensorRange)).thenReturn(sensorRange);
		String JSON = mapper.writeValueAsString(sensorRange);
		String response = mockMvc
				.perform(post(HOST + PORT + SENSOR_RANGE).contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(JSON, response);
	}

	@Test
	void testUpdateSensorRange_normalFlow_success() throws Exception {
		when(adminConsoleService.updateSensorRange(sensorRange)).thenReturn(sensorRange);
		String JSON = mapper.writeValueAsString(sensorRange);
		String response = mockMvc
				.perform(put(HOST + PORT + SENSOR_RANGE).contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(JSON, response);
	}

	@Test
	void addSensorEmails_normalFlow_success() throws Exception {
		when(adminConsoleService.addSensorEmails(sensorEmails)).thenReturn(sensorEmails);
		String JSON = mapper.writeValueAsString(sensorEmails);
		String response = mockMvc
				.perform(post(HOST + PORT + SENSOR_EMAILS).contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(JSON, response);
	}

	@Test
	void testUpdateSensorEmails_normalFlow_success() throws Exception {
		when(adminConsoleService.updateSensorEmails(sensorEmails)).thenReturn(sensorEmails);
		String JSON = mapper.writeValueAsString(sensorEmails);
		String response = mockMvc
				.perform(put(HOST + PORT + SENSOR_EMAILS).contentType(MediaType.APPLICATION_JSON).content(JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(JSON, response);
	}
}
