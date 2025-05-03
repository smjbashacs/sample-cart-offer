package com.springboot;

import com.springboot.controller.OfferRequest;
import com.springboot.teststeps.CartOfferTestSteps;
import com.springboot.utils.TestDataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CartOfferApplicationTests {

	private static TestDataUtil testDataUtil;

	@BeforeAll
	public static void testDataSetUp() throws Exception{
		testDataUtil = new TestDataUtil("testdata/ApplyCartOfferData.json");
	}

	@Test
	public void checkFlatXForOneSegment() throws Exception {

		OfferRequest offerRequest = testDataUtil.getOfferRequestTestObject("TC001");
		System.out.println(offerRequest.toString());
		HttpResponse<String> response = CartOfferTestSteps.addOffer(offerRequest);

		//Validate the response
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode(),"Validating response code");

	}

}


