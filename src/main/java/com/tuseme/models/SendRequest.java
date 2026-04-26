package com.tuseme.models;

import java.util.*;

/**
 * Request object for sending SMS messages.
 */
public class SendRequest {
    private final String content;
    private final String senderId;
    private final List<Map<String, String>> recipients;
    private final String type;
    private final String priority;
    private final Map<String, String> metadata;

    private SendRequest(Builder builder) {
        this.content = builder.content;
        this.senderId = builder.senderId;
        this.recipients = builder.recipients;
        this.type = builder.type;
        this.priority = builder.priority;
        this.metadata = builder.metadata;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("content", content);
        map.put("sender_id", senderId);
        map.put("recipients", recipients);
        map.put("type", type);
        map.put("priority", priority);
        if (metadata != null && !metadata.isEmpty()) {
            map.put("metadata", metadata);
        }
        return map;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String content;
        private String senderId = "TUSEME-LTD";
        private List<Map<String, String>> recipients = new ArrayList<>();
        private String type = "promotional";
        private String priority = "MEDIUM";
        private Map<String, String> metadata;

        public Builder content(String content) { this.content = content; return this; }
        public Builder senderId(String senderId) { this.senderId = senderId; return this; }
        public Builder type(MessageType type) { this.type = type.getValue(); return this; }
        public Builder priority(Priority priority) { this.priority = priority.name(); return this; }
        public Builder metadata(Map<String, String> metadata) { this.metadata = metadata; return this; }

        public Builder addRecipient(String msisdn) {
            Map<String, String> r = new LinkedHashMap<>();
            r.put("msisdn", msisdn);
            recipients.add(r);
            return this;
        }

        public Builder addRecipient(String msisdn, String name) {
            Map<String, String> r = new LinkedHashMap<>();
            r.put("msisdn", msisdn);
            r.put("name", name);
            recipients.add(r);
            return this;
        }

        public SendRequest build() {
            if (content == null || content.isEmpty()) throw new IllegalArgumentException("content is required");
            if (recipients.isEmpty()) throw new IllegalArgumentException("At least one recipient is required");
            return new SendRequest(this);
        }
    }
}
