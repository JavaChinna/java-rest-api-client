
/**
 * @author Chinna
 */
package com.javachinna.rest.client.model;

import java.time.LocalDate;

import lombok.Data;

/**
 * Product
 */
@Data
public class Product {
	private LocalDate validFrom;

	private String description;

	private String edition;

	private Long id;

	private String name;

	private String version;
}
