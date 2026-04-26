package com.tuseme.models;

public enum MessageType {
    TRANSACTIONAL("transactional"),
    PROMOTIONAL("promotional");

    private final String value;

    MessageType(String value) { this.value = value; }
    public String getValue() { return value; }
}
