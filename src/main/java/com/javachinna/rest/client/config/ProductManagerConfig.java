package com.javachinna.rest.client.config;

public record ProductManagerConfig(String baseUrl, String username, String password, String proxyHost,
                                   Integer proxyPort, String proxyUser, String proxyPass) {
}
