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

    public OfferRequest  getOfferRequestTestObject(String testCaseID, String dataID) throws Exception {

        //Retrieve test data based on test case ID
        JsonNode testCaseData = testDataJSON.get(testCaseID);
        if (testCaseData == null) throw new RuntimeException("Test case not found in test data json");

        //Retrieve offer data from the testcase data
        JsonNode offerDataNode = testCaseData.get(dataID);
        if (offerDataNode == null) throw new RuntimeException("offer data "+dataID+" is not present for the test case "+testCaseID);

        //Convert json to POJO and return the object (OfferRequest)
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(offerDataNode, OfferRequest.class);
    }

    public ApplyOfferRequest getApplyOfferRequestObject(String testCaseID, String dataID) throws Exception {

        //Retrieve test data based on test case ID
        JsonNode testCaseData = testDataJSON.get(testCaseID);
        if (testCaseData == null) throw new RuntimeException("Test case not found in test data json");

        //Retrieve apply cart data from the testcase data
        JsonNode offerDataNode = testCaseData.get(dataID);
        if (offerDataNode == null) throw new RuntimeException("cart data "+dataID+" is not present for the test case "+testCaseID);

        //Convert json to POJO and return the object (ApplyOfferRequest)
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeToValue(offerDataNode, ApplyOfferRequest.class);
    }

    public String getFieldValueAsString(String testCaseID, String fieldName){

        //Retrieve test data based on test case ID
        JsonNode testCaseData = testDataJSON.get(testCaseID);
        if (testCaseData == null) throw new RuntimeException("Test case not found in test data json");

        //Retrieve the field value
        JsonNode fieldNode = testCaseData.get(fieldName);
        if (fieldNode == null) throw new RuntimeException("Field data "+fieldName+" is not present for the test case "+testCaseID);

        if(fieldNode.isInt()){
            return String.valueOf(fieldNode);
        }
        else{
            return fieldNode.textValue();
        }
    }

}

