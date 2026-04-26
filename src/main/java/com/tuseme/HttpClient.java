package com.tuseme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuseme.exceptions.TusemeException;
import com.tuseme.exceptions.AuthenticationException;
import com.tuseme.exceptions.ValidationException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Low-level HTTP client with automatic authentication and retries.
 */
public class HttpClient {
    private static final String DEFAULT_BASE_URL = "https://api.tuseme.co.ke/api/v1";
    private static final int DEFAULT_TIMEOUT = 30000;
    private static final int DEFAULT_MAX_RETRIES = 3;

    private final String apiKey;
    private final String apiSecret;
    private final String baseUrl;
    private final int timeout;
    private final int maxRetries;
    private final Gson gson;

    private String accessToken;
    private long tokenExpiresAt;

    public HttpClient(String apiKey, String apiSecret, String baseUrl, int timeout, int maxRetries) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.baseUrl = baseUrl != null ? baseUrl : DEFAULT_BASE_URL;
        this.timeout = timeout > 0 ? timeout : DEFAULT_TIMEOUT;
        this.maxRetries = maxRetries > 0 ? maxRetries : DEFAULT_MAX_RETRIES;
        this.gson = new GsonBuilder().create();
    }

    public String getApiKey() { return apiKey; }

    private void authenticate() throws TusemeException {
        try {
            String body = gson.toJson(Map.of("api_key", apiKey, "api_secret", apiSecret));
            String response = rawRequest("POST", "/auth/login", body, false);
            Map<String, Object> data = gson.fromJson(response, Map.class);

            if (!data.containsKey("access_token")) {
                throw new AuthenticationException("Invalid credentials", 401);
            }

            accessToken = (String) data.get("access_token");
            double expiresIn = data.containsKey("expires_in") ? ((Number) data.get("expires_in")).doubleValue() : 3600;
            tokenExpiresAt = System.currentTimeMillis() + (long)(expiresIn * 1000) - 60000;
        } catch (IOException e) {
            throw new TusemeException("Authentication network error: " + e.getMessage(), 0);
        }
    }

    private synchronized void ensureAuth() throws TusemeException {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpiresAt) {
            authenticate();
        }
    }

    public String request(String method, String path, Object body) throws TusemeException {
        ensureAuth();

        TusemeException lastError = null;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String jsonBody = body != null ? gson.toJson(body) : null;
                return rawRequest(method, path, jsonBody, true);
            } catch (TusemeException e) {
                lastError = e;
                if (e.getStatusCode() >= 500 || e.getStatusCode() == 429) {
                    if (attempt < maxRetries) {
                        try { Thread.sleep((long)(500 * Math.pow(2, attempt - 1))); } catch (InterruptedException ignored) {}
                        continue;
                    }
                }
                throw e;
            } catch (IOException e) {
                lastError = new TusemeException("Network error: " + e.getMessage(), 0);
                if (attempt < maxRetries) {
                    try { Thread.sleep((long)(500 * Math.pow(2, attempt - 1))); } catch (InterruptedException ignored) {}
                    continue;
                }
            }
        }
        throw lastError;
    }

    private String rawRequest(String method, String path, String body, boolean withAuth) throws IOException, TusemeException {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "tuseme-java/1.0.0");
        if (withAuth && accessToken != null) {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

        if (body != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }

        int status = conn.getResponseCode();
        String responseBody = readStream(status >= 400 ? conn.getErrorStream() : conn.getInputStream());
        conn.disconnect();

        if (status == 401) throw new AuthenticationException(responseBody, 401);
        if (status == 400) throw new ValidationException(responseBody, 400);
        if (status >= 400) throw new TusemeException("API error: " + status, status);

        return responseBody;
    }

    private String readStream(InputStream is) throws IOException {
        if (is == null) return "{}";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    public Gson getGson() { return gson; }
}
