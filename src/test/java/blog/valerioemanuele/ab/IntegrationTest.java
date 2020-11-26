package blog.valerioemanuele.ab;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	@DisplayName("Empty number of steps returns client error")
	void test_emptyNumberOfSteps() {
		
		ResponseEntity<Void> response = testRestTemplate.exchange("/grid/{numberOfSteps}", HttpMethod.PUT, HttpEntity.EMPTY, Void.class, Collections.singletonMap("numberOfSteps", ""));
		assertTrue(response.getStatusCode().is4xxClientError());
	}
	
	@Test
	@DisplayName("Not valid number of steps returns client error")
	void test_notValidNumberOfSteps() {
		
		ResponseEntity<Void> response = testRestTemplate.exchange("/grid/{numberOfSteps}", HttpMethod.PUT, HttpEntity.EMPTY, Void.class, Collections.singletonMap("numberOfSteps", "123abc"));
		assertTrue(response.getStatusCode().is4xxClientError());
		
		response = testRestTemplate.exchange("/grid/{numberOfSteps}", HttpMethod.PUT, HttpEntity.EMPTY, Void.class, Map.of("numberOfSteps", "11.1"));
		assertTrue(response.getStatusCode().is4xxClientError());
	}
	
	@Test
	@DisplayName("Valid number of steps")
	void test_ValidNumberOfSteps() {
		
		ResponseEntity<Void> response = testRestTemplate.exchange("/grid/{numberOfSteps}", HttpMethod.PUT, HttpEntity.EMPTY, Void.class, Collections.singletonMap("numberOfSteps", "1000"));
		assertTrue(response.getStatusCode().is2xxSuccessful());
	}

}
