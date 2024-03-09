package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.UrlConstants.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import telran.probes.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.service.SensorEmailsProviderService;

@WebMvcTest
class SensorEmailsProviderControllerTest {
	private static final long ID_1 = 1;

	@MockBean
	SensorEmailsProviderService emailsService;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	void getEmailsForSensor_normalFlow_success() throws Exception {
		String[] expected = { "m1", "m2" };
		when(emailsService.getSensorEmails(ID_1)).thenReturn(expected);
		String expectedJSON = mapper.writeValueAsString(expected);
		String response = mockMvc.perform(get(HOST + PORT + SENSOR_EMAILS + Long.toString(ID_1)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedJSON, response);
	}

	@Test
	void getEmailsForSensor_notExists_exception() throws Exception {
		when(emailsService.getSensorEmails(ID_1)).thenThrow(new SensorNotFoundException());
		mockMvc.perform(get(HOST + PORT + SENSOR_EMAILS + Long.toString(ID_1))).andExpect(status().isNotFound());
	}

}
