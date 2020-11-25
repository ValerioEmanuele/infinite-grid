package blog.valerioemanuele.ab.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import blog.valerioemanuele.ab.service.GridService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = GridController.class)
@AutoConfigureMockMvc
class TestGridController {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private GridService gridService;

	@Test
	@DisplayName("Empty is passed instead of number of steps")
	void test_gridOfEmptyNumberOfSteps_returnsBadRequest() throws Exception {
		mvc.perform(put("/grid/{n}", "")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("A not valid number is passed")
	void test_NoValidNumber_shouldReturnBadRequest() throws Exception {
		
		mvc.perform(put("/grid/{n}", "11abc")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("A decimal number is passed")
	void test_decimalNumber_shouldReturnBadRequest() throws Exception {
		
		mvc.perform(put("/grid/{n}", "11.1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("A valid number of steps is passed")
	void test_decimalNumbera_shouldReturnBadRequest() throws Exception {
		
		doNothing().when(gridService).gridFor(BigInteger.valueOf(3));
		
		mvc.perform(put("/grid/{n}", "3")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}

}
