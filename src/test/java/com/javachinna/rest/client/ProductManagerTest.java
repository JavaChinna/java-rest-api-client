package com.javachinna.rest.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.javachinna.rest.client.config.ProductManagerConfig;
import com.javachinna.rest.client.model.ApiResponse;
import com.javachinna.rest.client.model.Product;
import com.javachinna.rest.client.model.ProductRequest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductManagerTest {
	
	ProductManagerConfig productManagerServiceConfig = new ProductManagerConfig(
			"http://localhost:8080/api/", "admin@javachinna.com", "admin@");
	ProductManager productManager = new ProductManagerImpl(productManagerServiceConfig);
	
	@Test
	@Order(1)
	void testCreateProduct() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setName("Dell Inspiron");
		request.setDescription("Inspiron 16 plus");
		request.setVersion("7620");
		request.setEdition("2021");
		request.setValidFrom(LocalDate.now());
		ApiResponse response = productManager.createProduct(request);
		assertNotNull(response);
		assertEquals(true, response.getSuccess());
	}

	@Test
	@Order(2)
	void testGetAllProducts() throws Exception {
		List<HashMap<String, Object>> list = productManager.getAllProducts();
		assertNotNull(list);
		assertEquals("7620", list.get(0).get("version"));
	}
	
	@Test
	@Order(3)
	void testGetProduct() throws Exception {
		Integer productId = getProductId();
		Product product = productManager.getProduct(productId);
		assertNotNull(product);
		assertEquals("7620", product.getVersion());
	}
	
	@Test
	@Order(4)
	void testUpdateProduct() throws Exception {
		Integer productId = getProductId();
		Product product = productManager.getProduct(productId);
		ProductRequest request = new ProductRequest();
		request.setName(product.getName());
		request.setDescription(product.getDescription());
		request.setVersion(product.getVersion());
		request.setValidFrom(product.getValidFrom());
		request.setEdition("2022");
		ApiResponse response = productManager.updateProduct(productId, request);
		assertNotNull(response);
		assertEquals(true, response.getSuccess());
		product = productManager.getProduct(productId);
		assertEquals("2022", product.getEdition());
	}

	private Integer getProductId() throws Exception {
		List<HashMap<String, Object>> list = productManager.getAllProducts();
		Integer productId = (Integer) list.get(0).get("id");
		return productId;
	}

	@Test
	@Order(5)
	void testDeleteProduct() throws Exception {
		Integer productId = getProductId();
		ApiResponse response = productManager.deleteProduct(productId);
		assertNotNull(response);
		assertEquals(true, response.getSuccess());
	}
}
