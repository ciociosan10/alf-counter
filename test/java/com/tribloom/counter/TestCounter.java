package com.tribloom.counter;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
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
	private static final Integer TEST_VALUE_2 = new Integer(0);
	private static final String FAKE_NAME = "fakeCounter";
	
	protected String getUrl() {
		return HOST + SERVICE;
	}
	
	@Test
	public void testPutInitializedCounter() {
		Response response = given().contentType(ContentType.JSON).body("{\"value\":" + TEST_VALUE_1 + "}")
				.auth().basic(USER, PASS).expect().statusCode(HttpStatus.SC_CREATED)
				.when().put(getUrl() + TEST_NAME_1);
		log.info(response.asString());
	}
	
	@Test
	public void testPutEmptyCounter() {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_CREATED).when().put(getUrl() + TEST_NAME_2);
		log.info(response.asString());
	}
	
	@Test
	public void testPutDuplicateEmptyCounter() {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_NOT_MODIFIED).when().put(getUrl() + TEST_NAME_2);
		log.info(response.asString());
	}
	
	@Test
	public void testPutDuplicateInitializedCounter() {
		Response response = given().contentType(ContentType.JSON).body("{\"value\":" + TEST_VALUE_1 + "}")
				.auth().basic(USER, PASS).expect().statusCode(HttpStatus.SC_NOT_MODIFIED)
				.when().put(getUrl() + TEST_NAME_1);
		log.info(response.asString());
	}
	
	@Test
	public void testGetInitializedCounter() throws Exception {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_1);
		JSONObject json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name"));
		assertTrue(json.has("value") == true);
		assertTrue(TEST_NAME_1.equals(json.get("name")));
		assertTrue(TEST_VALUE_1.equals(json.get("value")));
	}
	
	@Test
	public void testGetEmptyCounter() throws Exception {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_2);
		JSONObject json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name"));
		assertTrue(json.has("value") == true);
		assertTrue(TEST_NAME_2.equals(json.get("name")));
		assertTrue(TEST_VALUE_2.equals(json.get("value")));		
	}
	
	@Test
	public void testGetFakeCounter() {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_NOT_FOUND).when().get(getUrl() + FAKE_NAME);
		log.info(response.asString());
	}

	@Test
	public void testPostIncrementedCounter() throws Exception {
		Integer updatedValue = TEST_VALUE_1 + 1;
		Response response = given().contentType(ContentType.JSON)
				.body("{\"value\":" + updatedValue + "}")
				.auth().basic(USER, PASS).expect().statusCode(HttpStatus.SC_OK)
				.when().post(getUrl() + TEST_NAME_1);
		log.info(response.asString());
		
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().get(getUrl() + TEST_NAME_1);
		JSONObject json = new JSONObject(response.asString());
		log.info(json.toString(2));
		assertTrue(json.has("name"));
		assertTrue(json.has("value") == true);
		assertTrue(TEST_NAME_1.equals(json.get("name")));
		assertTrue(updatedValue.equals(json.get("value")));
	}
	
	@Test
	public void testPostEmptyCounter() {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_BAD_REQUEST).when()
				.post(getUrl() + TEST_NAME_1);
		log.info(response.asString());
	}
	
	@Test
	public void testPostFakeCounter() {
		Response response = given().contentType(ContentType.JSON).body("{\"value\":0}")
				.auth().basic(USER, PASS).expect().statusCode(HttpStatus.SC_NOT_FOUND)
				.when().post(getUrl() + FAKE_NAME);
		log.info(response.asString());
	}
	
	@Test
	public void testDeleteFakeCounter() throws Exception {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_NOT_FOUND).when().delete(getUrl() + FAKE_NAME);
		log.info(response.asString());
	}
	
	@Test
	public void testDeleteCounters() {
		Response response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().delete(getUrl() + TEST_NAME_1);
		log.info(response.asString());
		
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_OK).when().delete(getUrl() + TEST_NAME_2);
		log.info(response.asString());
		
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_NOT_FOUND).when().get(getUrl() + TEST_NAME_1);
		log.info(response.asString());
		
		response = given().auth().basic(USER, PASS).expect()
				.statusCode(HttpStatus.SC_NOT_FOUND).when().get(getUrl() + TEST_NAME_2);
		log.info(response.asString());	
	}
}
