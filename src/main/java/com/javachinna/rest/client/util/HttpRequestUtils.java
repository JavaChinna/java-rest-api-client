package com.javachinna.rest.client.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestUtils {

	private static final String CONTENT_TYPE = "Content-Type";
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
	private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	private HttpRequestUtils() {
	}

	public static <T> T get(String url, String token, Class<T> valueType)
			throws IOException, InterruptedException, JsonProcessingException, JsonMappingException {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader(AUTHORIZATION, BEARER + token).build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		return objectMapper.readValue(response.body(), valueType);
	}

	public static <T> T post(String uri, Object request, Class<T> valueType)
			throws JsonProcessingException, IOException, InterruptedException, JsonMappingException {
		return post(uri, request, valueType, null);
	}

	public static <T> T post(String uri, Object request, Class<T> valueType, String token)
			throws JsonProcessingException, IOException, InterruptedException, JsonMappingException {
		Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).POST(getBodyPublisher(request)).header(CONTENT_TYPE, "application/json");
		return send(valueType, token, builder);
	}

	public static <T> T put(String uri, Object request, Class<T> valueType, String token)
			throws JsonProcessingException, IOException, InterruptedException, JsonMappingException {
		Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).PUT(getBodyPublisher(request)).header(CONTENT_TYPE, "application/json");
		return send(valueType, token, builder);
	}
	
	public static <T> T delete(String uri, Class<T> valueType, String token)
			throws JsonProcessingException, IOException, InterruptedException, JsonMappingException {
		Builder builder = HttpRequest.newBuilder().uri(URI.create(uri)).DELETE();
		return send(valueType, token, builder);
	}

	private static BodyPublisher getBodyPublisher(Object request) throws JsonProcessingException {
		return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request));
	}

	private static <T> T send(Class<T> valueType, String token, Builder builder)
			throws IOException, InterruptedException, JsonProcessingException, JsonMappingException {
		builder.header(CONTENT_TYPE, "application/json");
		if (token != null) {
			builder.header(AUTHORIZATION, BEARER + token);
		}
		HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			throw new RuntimeException(response.statusCode() + " : " + response.body());
		}
		return objectMapper.readValue(response.body(), valueType);
	}
}