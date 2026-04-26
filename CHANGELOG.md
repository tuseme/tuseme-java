# Changelog

All notable changes to the Tuseme Java SDK will be documented in this file.

## [1.0.0] - 2026-04-25

### Added
- Initial release of the Tuseme Java SDK.
- `TusemeClient` with builder pattern, automatic authentication, and token refresh.
- `messages().send()` — send SMS to one or more recipients.
- `messages().get()` — check delivery status of a message.
- `messages().list()` — list sent messages with pagination.
- Built-in retry logic with exponential backoff.
- Type-safe enums for `MessageType` and `Priority`.
- Exception hierarchy: `AuthenticationException`, `ValidationException`, `TusemeException`.
- Java 11+ with zero external dependencies beyond Gson.
