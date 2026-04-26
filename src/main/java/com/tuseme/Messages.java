package com.tuseme;

import com.tuseme.models.SendRequest;
import com.tuseme.models.SendResponse;
import com.tuseme.exceptions.TusemeException;

/**
 * Messages API resource.
 */
public class Messages {
    private final HttpClient http;

    public Messages(HttpClient http) {
        this.http = http;
    }

    /**
     * Send an SMS to one or more recipients.
     */
    public SendResponse send(SendRequest request) throws TusemeException {
        String response = http.request("POST", "/messages/send", request.toMap());
        return SendResponse.fromJson(response, http.getGson());
    }

    /**
     * Get delivery status for a message.
     */
    public String get(String messageId) throws TusemeException {
        return http.request("GET", "/messages/" + messageId, null);
    }

    /**
     * List sent messages.
     */
    public String list(int page, int pageSize) throws TusemeException {
        return http.request("GET", "/messages?page=" + page + "&page_size=" + pageSize, null);
    }
}
