package com.javachinna.rest.client.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
 
/**
 * 
 * @author Chinna [javachinna.com]
 *
 */
public class HttpRequestHelper {
 
    private final String APPLICATION_JSON = "application/json";
	private final String CONTENT_TYPE = "Content-Type";
    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final HttpClient httpClient;
 
    public HttpRequestHelper(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }
 
    public <T> T get(String url, String token, Class<T> valueType)
            throws IOException, InterruptedException {
    	Builder builder = HttpRequest.newBuilder().GET().uri(URI.create(url));
        return send(valueType, token, builder);
    }
 
    public <T> T post(String uri, Object request, Class<T> valueType)
            throws IOException, InterruptedException {
        return post(uri, request, valueType, null);
    }
 
    public <T> T post(String uri, Object request, Class<T> valueType, String token)
            throws IOException, InterruptedException {
        Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).POST(getBodyPublisher(request)).header(CONTENT_TYPE, APPLICATION_JSON);
        return send(valueType, token, builder);
    }
 
    public <T> T put(String uri, Object request, Class<T> valueType, String token)
            throws IOException, InterruptedException {
        Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).PUT(getBodyPublisher(request)).header(CONTENT_TYPE, APPLICATION_JSON);
        return send(valueType, token, builder);
    }
    
    public <T> T patch(String uri, Class<T> valueType, String token)
    		throws IOException, InterruptedException {
    	Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).method("PATCH", HttpRequest.BodyPublishers.noBody()).header(CONTENT_TYPE, APPLICATION_JSON);
    	return send(valueType, token, builder);
    }
     
    public <T> T delete(String uri, Class<T> valueType, String token)
            throws IOException, InterruptedException {
        Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).DELETE();
        return send(valueType, token, builder);
    }
 
    private BodyPublisher getBodyPublisher(Object request) throws JsonProcessingException {
        return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request));
    }
 
    private <T> T send(Class<T> valueType, String token, Builder builder)
            throws IOException, InterruptedException {
        if (token != null) {
            builder.header(AUTHORIZATION, BEARER + token);
        }
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException(response.body());
        }
        return objectMapper.readValue(response.body(), valueType);
    }
}
