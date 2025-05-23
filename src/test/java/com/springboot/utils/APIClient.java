package com.springboot.utils;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpResponse<String> postRequest(String url, String jsonBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> getRequest(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
