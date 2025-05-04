package com.springboot.apitests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.OfferRequest;
import com.springboot.teststeps.CartOfferTestSteps;
import com.springboot.utils.TestDataUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartOfferApplicationTests {

	@Autowired
	private CartOfferTestSteps cartOfferTestSteps;

	private static TestDataUtil testDataUtil;
	private static ExtentReports extentReport;

	@BeforeAll
	public static void setUp() throws Exception{

		//Initialise testDataUtil - to parse and read test data
		testDataUtil = new TestDataUtil("testdata/ApplyCartOfferData.json");

		//HTML extent report configuration
		ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ApplyCartOfferTestReport.html");
		extentReport = new ExtentReports();
		extentReport.attachReporter(spark);
	}

	@AfterAll
	public static void closeTestRun() {
		extentReport.flush();
	}

	@Test @Order(1)
	public void sanityTest_AddOfferAPI() throws Exception {

		ExtentTest test = extentReport.createTest("sanityTest_AddOfferAPI");
		try{
			//Check if FLATX offer can be applied to restaurant
			OfferRequest offerRequest_1 = testDataUtil.getOfferRequestTestObject("sanity_test","offer_data_1");
			HttpResponse<String> response_1 = cartOfferTestSteps.addOffer(offerRequest_1);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, response_1.statusCode(),"Validating response code");

			//Check if FLATX_PERCENT offer can be applied to restaurant
			OfferRequest offerRequest_2 = testDataUtil.getOfferRequestTestObject("sanity_test","offer_data_2");
			HttpResponse<String> response_2 = cartOfferTestSteps.addOffer(offerRequest_2);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, response_2.statusCode(),"Validating response code");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(2)
	public void sanityTest_GetUserSegmentAPI() throws Exception {

		ExtentTest test = extentReport.createTest("sanityTest_GetUserSegmentAPI");
		try{
			//Retrieve test data
			String user_id = testDataUtil.getFieldValueAsString("sanity_test","user_id");
			String expectedUserSegment = testDataUtil.getFieldValueAsString("sanity_test","user_segment");

			//Use GET request to retrieve user segment based on user_id
			HttpResponse<String> response = cartOfferTestSteps.getUserSegment(user_id);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode(),"Validating response code");

			//Retrieve user segment details from response and validate
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(response.body());
			String userSegment = jsonNode.get("segment").asText();
			Assertions.assertEquals(expectedUserSegment, userSegment, "Check user segment");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(3)
	public void testNOOFFERForRestaurant() throws Exception{

		ExtentTest test = extentReport.createTest("testNoOfferForRestaurant");
		try{
			//Retrieve test data
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC002","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC002","expected_cart_value");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(4)
	public void testFLATXOffer() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXOffer: Offer value between 0 and cart value");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC002","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC002","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC002","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(5)
	public void testFLATXOffer_ZeroValue() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXOffer: Offer value is Zero");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC003","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC003","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC003","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(6)
	public void testFLATXOffer_offerExceedingCartValue() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXOffer: Offer value exceeding cart value");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC004","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC004","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC004","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(7)
	public void testFLATXPERCENTOffer() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXPERCENTOffer: Offer value between 0 and cart value");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC005","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC005","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC005","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(8)
	public void testFLATXPERCENTOffer_ZeroValue() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXPERCENTOffer: Offer value is Zero");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC006","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC006","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC006","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(9)
	public void testFLATXPERCENTOffer_100PERCENT() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXPERCENTOffer: Offer value is 100 Percent");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC007","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC007","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC007","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

	@Test @Order(10)
	public void testFLATXPERCENTOffer_MultipleOrdersBySingleUser() throws Exception{

		ExtentTest test = extentReport.createTest("TestFLAXPERCENTOffer: Multiple orders by single user");
		try{
			//Retrieve test data
			OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC008","offer_data");
			ApplyOfferRequest applyOfferRequest = testDataUtil.getApplyOfferRequestObject("TC008","cart_data");
			String expectedCartValue = testDataUtil.getFieldValueAsString("TC008","expected_cart_value");

			//Add offer to restaurant using GET request
			HttpResponse<String> addOfferResponse = cartOfferTestSteps.addOffer(offerRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, addOfferResponse.statusCode(),"Validating response code for adding offer");

			//Apply offer to cart using POST request - First Order
			HttpResponse<String> applyOfferResponse = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value - First Order
			String cartValue = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse);
			Assertions.assertEquals(expectedCartValue, cartValue, "Validating the final cart value after offer");

			//Apply offer to cart using POST request - Second Order
			HttpResponse<String> applyOfferResponse_2 = cartOfferTestSteps.applyCartOffer(applyOfferRequest);
			Assertions.assertEquals(HttpURLConnection.HTTP_OK, applyOfferResponse_2.statusCode(),"Validating response code for applying offer to cart");

			//Validate the final cart value - Second Order
			String cartValue_2 = cartOfferTestSteps.getCartValueFromResponse(applyOfferResponse_2);
			Assertions.assertEquals(expectedCartValue, cartValue_2, "Validating the final cart value after offer");
		}
		catch (Throwable t) {
			test.fail("Test failed: " + t.getMessage()); //To add test fail to extent report
			throw t; // Rethrow to let JUnit mark the test as failed
		}
	}

}


