package com.javachinna.rest.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {
	private Boolean success;
	private String message;
}