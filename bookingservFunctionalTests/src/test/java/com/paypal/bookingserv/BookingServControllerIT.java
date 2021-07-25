package com.paypal.bookingserv;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.bookingserv.api.model.Booking;

/**
 * 
 * Functional tests for BookingResourceImpl.java using TestNG
 * 
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
		"spring.datasource.url=jdbc:h2:file:./test" })
public class BookingServControllerIT extends AbstractTestNGSpringContextTests {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();
	HttpEntity<String> entity = new HttpEntity<String>(null, headers);

	/**
	 * 
	 * Tests SUCCESS(200) use case scenario for CreateBooking API
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void testCreateBooking200Status() throws JsonParseException, JsonMappingException, IOException {
		Booking testObj = loadTestBookingEntity("requests/CreateBookingTest200.json");
		ResponseEntity<Booking> resp = restTemplate.postForEntity("http://localhost:8080" + "/v1/bfs/booking", testObj,
				Booking.class);
		System.out.println(resp.getBody());
		// Check for a 200 response status
		assertEquals(200, resp.getStatusCodeValue());
		// Check for a booking id in the return value
		assertNotNull(resp.getBody().getId());
	}

	/**
	 * Tests BAD_REQUEST(400) use case scenario for CreateBooking API
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void testCreateBooking400Status() throws JsonParseException, JsonMappingException, IOException {
		Booking testObj = loadTestBookingEntity("requests/CreateBookingTest400.json");
		ResponseEntity<String> resp = restTemplate.postForEntity("http://localhost:" + port + "/v1/bfs/booking",
				testObj, String.class);
		System.out.println(resp);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode respObject = mapper.readTree(resp.getBody());
		// Check for 400 status
		assertEquals(400, resp.getStatusCodeValue());

		// Check for subErrors
		JsonNode expectedError = loadApiErrorEntity("responses/CreateBookingTest400Response.json");
		assertEquals(expectedError.size(), respObject.get("subErrors").size());

	}

	/**
	 * Tests BAD_REQUEST(400) due to DuplicateRequests use case scenario for
	 * CreateBooking API
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void testCreateBookingDuplicateStatus() throws JsonParseException, JsonMappingException, IOException {
		Booking testObj = loadTestBookingEntity("requests/CreateBookingTestDuplicate.json");
		ResponseEntity<String> resp = restTemplate.postForEntity("http://localhost:" + port + "/v1/bfs/booking",
				testObj, String.class);
		System.out.println(resp);
	}

	/**
	 * Tests GetBookings API
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	// Run this test after createBooking200 test so that there will be a entry in DB
	@Test
	public void testGetBookings() throws JsonParseException, JsonMappingException, IOException {
		String resp = restTemplate.getForObject("http://localhost:" + port + "/v1/bfs/booking", String.class);
		System.out.println(resp);
		ObjectMapper mapper = new ObjectMapper();
		List<Booking> responseList = mapper.readValue(resp, new TypeReference<List<Booking>>() {
		});
		// Check for list size
		assertEquals(1, responseList.size());
	}
	
	/**
	 * Method to load Booking requests from json files
	 * @param fileName - json filename to load
	 * @return Booking entity 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	private Booking loadTestBookingEntity(String fileName)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
		return objectMapper.readValue(file, Booking.class);

	}

	/**
	 * Method to load Error response from json files
	 * @param fileName - json file to load
	 * @return JsonNode containing error response
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private JsonNode loadApiErrorEntity(String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
		return objectMapper.readValue(file, JsonNode.class);

	}

}
