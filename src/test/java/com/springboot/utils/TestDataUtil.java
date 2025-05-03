package com.springboot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.OfferRequest;

import java.io.InputStream;

public class TestDataUtil {

    private final JsonNode testDataJSON;

    public TestDataUtil(String testDataFilePath) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = TestDataUtil.class.getClassLoader().getResourceAsStream(testDataFilePath);
        testDataJSON = mapper.readTree(inputStream);
    }

    public OfferRequest  getOfferRequestTestObject(String testCaseID) throws Exception {

        //Retrieve test data based on test case ID
        JsonNode testCaseData = testDataJSON.get(testCaseID);
        if (testCaseData == null) throw new RuntimeException("Test case not found in test data json");

        //Retrieve offer data from the testcase data
        JsonNode offerDataNode = testCaseData.get("offer_data");
        if (offerDataNode == null) throw new RuntimeException("Offer data is not present for the test case");

        //Convert json to POJO and return the object (OfferRequest)
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(offerDataNode, OfferRequest.class);
    }

    public ApplyOfferRequest getApplyOfferRequestObject(String testCaseID) throws Exception {

        //Retrieve test data based on test case ID
        JsonNode testCaseData = testDataJSON.get(testCaseID);
        if (testCaseData == null) throw new RuntimeException("Test case not found in test data json");

        //Retrieve apply cart data from the testcase data
        JsonNode offerDataNode = testCaseData.get("cart_data");
        if (offerDataNode == null) throw new RuntimeException("cart data is not present for the test case");

        //Convert json to POJO and return the object (ApplyOfferRequest)
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(offerDataNode, ApplyOfferRequest.class);
    }

}

