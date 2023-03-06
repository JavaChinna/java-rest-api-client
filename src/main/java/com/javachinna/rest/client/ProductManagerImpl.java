package com.javachinna.rest.client;

import com.javachinna.rest.client.config.ProductManagerConfig;
import com.javachinna.rest.client.helper.HttpRequestHelper;
import com.javachinna.rest.client.model.*;
import lombok.RequiredArgsConstructor;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ProductManagerImpl implements ProductManager {
    private static final String AUTH_API = "auth/signin";
    private static final String PRODUCT_API = "products";

    private final ProductManagerConfig productManagerConfig;
    private final HttpRequestHelper requestHelper;

    public ProductManagerImpl(ProductManagerConfig config) {
        this.productManagerConfig = config;
        HttpClient.Builder builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10));

        if (config.proxyHost() != null && config.proxyPort() != null) {
            builder.proxy(ProxySelector.of(new InetSocketAddress(config.proxyHost(), config.proxyPort())));
            Authenticator authenticator = Authenticator.getDefault();
            if (config.proxyUser() != null && config.proxyPass() != null) {
                authenticator = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.proxyUser(), config.proxyPass().toCharArray());
                    }
                };
            }
            builder.authenticator(authenticator);
        }
        this.requestHelper = new HttpRequestHelper(builder.build());
    }

    public Product getProduct(Integer productId) throws Exception {
        String url = productManagerConfig.baseUrl() + PRODUCT_API + "/" + productId;
        return requestHelper.get(url, getAccessToken(), Product.class);
    }

    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getAllProducts() throws Exception {
        String url = productManagerConfig.baseUrl() + PRODUCT_API;
        Map<String, Object> response = requestHelper.get(url, getAccessToken(), Map.class);
        return (List<HashMap<String, Object>>) response.get("content");
    }

    public ApiResponse createProduct(ProductRequest request) throws Exception {
        return requestHelper.post(productManagerConfig.baseUrl() + PRODUCT_API, request, ApiResponse.class,
                getAccessToken());
    }

    public ApiResponse updateProduct(Integer productId, ProductRequest request) throws Exception {
        return requestHelper.put(productManagerConfig.baseUrl() + PRODUCT_API + "/" + productId, request,
                ApiResponse.class, getAccessToken());
    }

    public ApiResponse deleteProduct(Integer productId) throws Exception {
        return requestHelper.delete(productManagerConfig.baseUrl() + PRODUCT_API + "/" + productId,
                ApiResponse.class, getAccessToken());
    }

    public String getAccessToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest(productManagerConfig.username(),
                productManagerConfig.password());
        JwtAuthenticationResponse jwtResponse = requestHelper.post(productManagerConfig.baseUrl() + AUTH_API,
                loginRequest, JwtAuthenticationResponse.class);
        return jwtResponse.getAccessToken();
    }
}
