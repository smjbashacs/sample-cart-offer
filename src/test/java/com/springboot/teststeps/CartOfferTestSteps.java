package com.springboot.teststeps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.OfferRequest;
import com.springboot.utils.APIClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CartOfferTestSteps {

    public static HttpResponse<String> addOffer(OfferRequest offerRequest) throws Exception {

        String urlString = "http://localhost:9001/api/v1/offer";
        ObjectMapper mapper = new ObjectMapper();
        String POST_REQUEST_BODY_JSON = mapper.writeValueAsString(offerRequest);
        return APIClient.postRequest(urlString, POST_REQUEST_BODY_JSON);

    }

}
