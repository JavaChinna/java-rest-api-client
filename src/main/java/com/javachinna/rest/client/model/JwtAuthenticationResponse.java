package com.javachinna.rest.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthenticationResponse {
	private String accessToken;
}