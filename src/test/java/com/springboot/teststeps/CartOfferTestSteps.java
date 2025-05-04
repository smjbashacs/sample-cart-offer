package com.springboot.teststeps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.OfferRequest;
import com.springboot.utils.APIClient;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpResponse;

@Component
public class CartOfferTestSteps {

    @Value("${application.base_url}")
    private String base_url;

    @Value("${application.endpoints.add_offer}")
    private String addOfferEndpoint;

    @Value("${application.endpoints.apply_cart_offer}")
    private String applyCartOfferEndpoint;

    @Value("${application.user_segment_mock_url}")
    private String userSegmentMockUrl;

    public HttpResponse<String> addOffer(OfferRequest offerRequest) throws Exception {

        String urlString = base_url+addOfferEndpoint;
        ObjectMapper mapper = new ObjectMapper();
        String POST_REQUEST_BODY_JSON = mapper.writeValueAsString(offerRequest);
        return APIClient.postRequest(urlString, POST_REQUEST_BODY_JSON);
    }

    public HttpResponse<String> applyCartOffer(ApplyOfferRequest applyOfferRequest) throws Exception {

        String urlString = base_url+applyCartOfferEndpoint;
        ObjectMapper mapper = new ObjectMapper();
        String POST_REQUEST_BODY_JSON = mapper.writeValueAsString(applyOfferRequest);
        return APIClient.postRequest(urlString, POST_REQUEST_BODY_JSON);
    }

    public HttpResponse<String> getUserSegment(String user_id) throws Exception {

        String urlString = userSegmentMockUrl+"?user_id="+user_id;
        return APIClient.getRequest(urlString);
    }

    public String getCartValueFromResponse(HttpResponse<String> response) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body());
        return jsonNode.get("cart_value").asText();
    }

}
