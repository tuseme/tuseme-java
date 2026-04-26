package com.tuseme;

import com.tuseme.models.SendRequest;
import com.tuseme.models.SendResponse;
import com.tuseme.exceptions.TusemeException;

/**
 * Official Tuseme SMS API client for Java.
 *
 * <pre>
 * TusemeClient client = TusemeClient.builder()
 *     .apiKey("tk_test_...")
 *     .apiSecret("sk_test_...")
 *     .build();
 *
 * SendResponse response = client.messages().send(
 *     SendRequest.builder()
 *         .content("Hello from Tuseme!")
 *         .senderId("TUSEME-LTD")
 *         .addRecipient("+254712345678", "John Doe")
 *         .build()
 * );
 * </pre>
 */
public class TusemeClient {
    private final HttpClient httpClient;
    private final Messages messages;

    private TusemeClient(Builder builder) {
        this.httpClient = new HttpClient(
            builder.apiKey,
            builder.apiSecret,
            builder.baseUrl,
            builder.timeout,
            builder.maxRetries
        );
        this.messages = new Messages(this.httpClient);
    }

    public Messages messages() { return messages; }
    public boolean isSandbox() { return httpClient.getApiKey().startsWith("tk_test_"); }
    public boolean isProduction() { return httpClient.getApiKey().startsWith("tk_live_"); }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String apiKey;
        private String apiSecret;
        private String baseUrl = "https://api.tuseme.co.ke/api/v1";
        private int timeout = 30000;
        private int maxRetries = 3;

        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }
        public Builder apiSecret(String apiSecret) { this.apiSecret = apiSecret; return this; }
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder timeout(int timeout) { this.timeout = timeout; return this; }
        public Builder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }

        public TusemeClient build() {
            if (apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("apiKey is required");
            if (apiSecret == null || apiSecret.isEmpty()) throw new IllegalArgumentException("apiSecret is required");
            return new TusemeClient(this);
        }
    }
}
