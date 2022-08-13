package com.javachinna.rest.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javachinna.rest.client.config.ProductManagerConfig;
import com.javachinna.rest.client.model.ApiResponse;
import com.javachinna.rest.client.model.JwtAuthenticationResponse;
import com.javachinna.rest.client.model.LoginRequest;
import com.javachinna.rest.client.model.Product;
import com.javachinna.rest.client.model.ProductRequest;
import com.javachinna.rest.client.util.HttpRequestUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductManagerImpl implements ProductManager {
	private static final String AUTH_API = "auth/signin";
	private static final String PRODUCT_API = "products";

	private final ProductManagerConfig productManagerConfig;

	public Product getProduct(Integer productId) throws Exception {
		String url = productManagerConfig.getBaseUrl() + PRODUCT_API + "/" + productId;
		return HttpRequestUtils.get(url, getAccessToken(), Product.class);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> getAllProducts() throws Exception {
		String url = productManagerConfig.getBaseUrl() + PRODUCT_API;
		Map<String, Object> response = HttpRequestUtils.get(url, getAccessToken(), Map.class);
		return (List<HashMap<String, Object>>) response.get("content");
	}

	public ApiResponse createProduct(ProductRequest request) throws Exception {
		return HttpRequestUtils.post(productManagerConfig.getBaseUrl() + PRODUCT_API, request, ApiResponse.class,
				getAccessToken());
	}

	public ApiResponse updateProduct(Integer productId, ProductRequest request) throws Exception {
		return HttpRequestUtils.put(productManagerConfig.getBaseUrl() + PRODUCT_API + "/" + productId, request,
				ApiResponse.class, getAccessToken());
	}

	public ApiResponse deleteProduct(Integer productId) throws Exception {
		return HttpRequestUtils.delete(productManagerConfig.getBaseUrl() + PRODUCT_API + "/" + productId,
				ApiResponse.class, getAccessToken());
	}

	public String getAccessToken() throws Exception {
		LoginRequest loginRequest = new LoginRequest(productManagerConfig.getUsername(),
				productManagerConfig.getPassword());
		JwtAuthenticationResponse jwtResponse = HttpRequestUtils.post(productManagerConfig.getBaseUrl() + AUTH_API,
				loginRequest, JwtAuthenticationResponse.class);
		return jwtResponse.getAccessToken();
	}
}
