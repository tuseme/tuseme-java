# Contributing to Tuseme Java SDK

Thank you for your interest in contributing!

## Development Setup

```bash
git clone https://github.com/tuseme/tuseme-java.git
cd tuseme-java
mvn clean install
```

## Running Tests

```bash
mvn test
```

## Pull Requests

1. Fork the repo and create a feature branch from `main`.
2. Add tests for any new functionality.
3. Ensure `mvn clean verify` passes.
4. Open a PR with a clear description of the change.

## Reporting Issues

Open an issue at [github.com/tuseme/tuseme-java/issues](https://github.com/tuseme/tuseme-java/issues) with:
- SDK version and JDK version
- Minimal reproduction steps
- Expected vs. actual behavior
