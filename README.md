# Tuseme Java SDK

Official Java client for the [Tuseme SMS API](https://docs.tuseme.co.ke).

[![Maven Central](https://img.shields.io/maven-central/v/com.tuseme/sdk)](https://search.maven.org/artifact/com.tuseme/sdk)
[![Java 11+](https://img.shields.io/badge/java-11+-blue.svg)](https://adoptium.net)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## Installation

### Maven

```xml
<dependency>
    <groupId>com.tuseme</groupId>
    <artifactId>sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.tuseme:sdk:1.0.0'
```

## Quick Start

```java
import com.tuseme.TusemeClient;
import com.tuseme.models.*;

TusemeClient client = TusemeClient.builder()
    .apiKey("tk_test_your_api_key")
    .apiSecret("sk_test_your_api_secret")
    .build();

SendResponse response = client.messages().send(
    SendRequest.builder()
        .content("Hello from Tuseme! Your OTP is 482910.")
        .senderId("TUSEME-LTD")
        .addRecipient("+254712345678", "John Doe")
        .type(MessageType.TRANSACTIONAL)
        .priority(Priority.HIGH)
        .build()
);

System.out.println("Message ID: " + response.getMessageId());
System.out.println("Status: " + response.getStatus());
```

## Features

- **Builder pattern** — fluent, type-safe API
- **Automatic authentication** — tokens obtained and refreshed transparently
- **Built-in retries** — exponential backoff for transient failures
- **Type-safe enums** — `MessageType` and `Priority`
- **Java 11+** compatible with Gson dependency

## Authentication

```java
// Sandbox credentials (for testing)
TusemeClient client = TusemeClient.builder()
    .apiKey("tk_test_...")
    .apiSecret("sk_test_...")
    .build();

// Production credentials
TusemeClient client = TusemeClient.builder()
    .apiKey("tk_live_...")
    .apiSecret("sk_live_...")
    .build();
```

The SDK will:
1. Automatically obtain an access token on the first request
2. Cache the token until it expires
3. Transparently refresh expired tokens

## Usage

### Send SMS

```java
// Single recipient
SendResponse response = client.messages().send(
    SendRequest.builder()
        .content("Your verification code is 123456")
        .senderId("TUSEME-LTD")
        .addRecipient("+254712345678")
        .type(MessageType.TRANSACTIONAL)
        .build()
);

// Multiple recipients with metadata
SendResponse response = client.messages().send(
    SendRequest.builder()
        .content("Flash sale! 50% off today only.")
        .senderId("TUSEME-LTD")
        .addRecipient("+254712345678", "Alice")
        .addRecipient("+254798765432", "Bob")
        .type(MessageType.PROMOTIONAL)
        .priority(Priority.MEDIUM)
        .metadata(Map.of("campaign", "flash_sale_q2"))
        .build()
);
```

### Check Delivery Status

```java
MessageStatus status = client.messages().get("msg_a1b2c3d4...");
System.out.println("Status: " + status.getStatus());
System.out.println("Delivered at: " + status.getDeliveredAt());
```

### List Messages

```java
List<MessageStatus> messages = client.messages().list(1, 20);
messages.forEach(msg ->
    System.out.printf("%s: %s%n", msg.getRecipient(), msg.getStatus())
);
```

## Error Handling

```java
import com.tuseme.exceptions.*;

try {
    SendResponse response = client.messages().send(request);
} catch (AuthenticationException e) {
    System.err.println("Invalid credentials — check your API key and secret");
} catch (ValidationException e) {
    System.err.println("Bad request: " + e.getMessage());
} catch (TusemeException e) {
    System.err.println("API error: " + e.getMessage());
}
```

## Configuration

```java
TusemeClient client = TusemeClient.builder()
    .apiKey("...")
    .apiSecret("...")
    .baseUrl("https://api.tuseme.co.ke/api/v1")  // default
    .timeout(30)       // request timeout in seconds
    .maxRetries(3)     // automatic retries on failure
    .build();
```

## License

MIT — see [LICENSE](LICENSE).
