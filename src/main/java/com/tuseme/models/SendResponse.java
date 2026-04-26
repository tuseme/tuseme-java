package com.tuseme.models;

import com.google.gson.Gson;

/**
 * Response from sending a message.
 */
public class SendResponse {
    private boolean success;
    private String message_id;
    private String batch_id;
    private String status;
    private String message;
    private Double estimated_cost;
    private String currency;
    private String selected_provider;
    private int recipient_count;
    private String timestamp;

    public boolean isSuccess() { return success; }
    public String getMessageId() { return message_id; }
    public String getBatchId() { return batch_id; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Double getEstimatedCost() { return estimated_cost; }
    public String getCurrency() { return currency; }
    public String getSelectedProvider() { return selected_provider; }
    public int getRecipientCount() { return recipient_count; }
    public String getTimestamp() { return timestamp; }

    public static SendResponse fromJson(String json, Gson gson) {
        return gson.fromJson(json, SendResponse.class);
    }

    @Override
    public String toString() {
        return "SendResponse{messageId=" + message_id + ", status=" + status + ", batchId=" + batch_id + "}";
    }
}
