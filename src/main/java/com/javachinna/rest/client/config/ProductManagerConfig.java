package com.javachinna.rest.client.config;

import lombok.Value;

@Value
public class ProductManagerConfig {
	private String baseUrl;
	private String username;
	private String password;
}
