package com.javachinna.rest.client;

import java.util.HashMap;
import java.util.List;

import com.javachinna.rest.client.model.ApiResponse;
import com.javachinna.rest.client.model.Product;
import com.javachinna.rest.client.model.ProductRequest;

public interface ProductManager {

	Product getProduct(Integer l) throws Exception;

	List<HashMap<String, Object>> getAllProducts() throws Exception;

	ApiResponse createProduct(ProductRequest request) throws Exception;

	ApiResponse updateProduct(Integer productId, ProductRequest request) throws Exception;

	ApiResponse deleteProduct(Integer productId) throws Exception;

	String getAccessToken() throws Exception;

}