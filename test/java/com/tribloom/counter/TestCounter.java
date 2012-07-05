package com.tribloom.counter;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * This is an Integration Test class that will test the Demo Counter GET webscript.
 * 
 * This test class uses the Rest-Assured library for RESTful interactions.
 * For more information on Rest-Assured, see:
 *   http://code.google.com/p/rest-assured/wiki 
 * 
 * @author Chris Paul, Tribloom
 */
public class TestCounter extends RestAssured {
	
	private static final Logger log = Logger.getLogger(TestCounter.class.toString());

	private static final String HOST = "http://localhost:8080";
	private static final String USER = "admin";
	private static final String PASS = "admin";
	
	private static final String SERVICE = "/alfresco/service/counter/";
	
	private static final String TEST_NAME_1 = "counter1";
	private static final Integer TEST_VALUE_1 = new Integer(2);
	private static final String TEST_NAME_2 = "counter2";
	private static final Integer TEST_VALUE_2 = new Integer(1);
	
	protected String getUrl() {
		return HOST + SERVICE;
	}
	
	@Test
	public void testGetInitializedCounter() throws Exception {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_1 + "?value=" + TEST_VALUE_1);
		JSONObject json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_1, json.get("name"));
		assertEquals(TEST_VALUE_1, json.get("value"));
		
		// Verify value increments
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_1);
		json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_1, json.get("name"));
		assertEquals(TEST_VALUE_1 + 1, json.get("value"));
		
		// Test reset
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_1 + "?value=" + TEST_VALUE_1);
		json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_1, json.get("name"));
		assertEquals(TEST_VALUE_1, json.get("value"));
		
		// Verify value increments
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_1);
		json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_1, json.get("name"));
		assertEquals(TEST_VALUE_1 + 1, json.get("value"));
	}
	
	@Test
	public void testGetEmptyCounter() throws Exception {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_2);
		JSONObject json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_2, json.get("name"));
		assertEquals(TEST_VALUE_2, json.get("value"));		
		
		// Verify that value increments automatically
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_2);
		json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_2, json.get("name"));
		assertEquals(TEST_VALUE_2 + 1, json.get("value"));
		
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_2);
		json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name") && json.has("value"));
		assertEquals(TEST_NAME_2, json.get("name"));
		assertEquals(TEST_VALUE_2 + 2, json.get("value"));		
	}
	
	@Test
	public void testDeleteCounters() {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().delete(getUrl() + TEST_NAME_1);
		log.info(response.asString());
		
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().delete(getUrl() + TEST_NAME_2);
		log.info(response.asString());
	}
}
