package com.javachinna.rest.client.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductRequest {
	private String name;
	private String version;
	private String edition;
	private String description;
	private LocalDate validFrom;
}
