## Contributing

Thank you for your interest in contributing to this project

We welcome bug reports, feature requests, documentation improvements, tests, and code contributions.

### Development Requirements

Before contributing, make sure you have:

- Java 21
- Maven 3.9 or later
- Git

## Getting Started

Clone the repository

```terminaloutput
git clone https://github.com/solmosov/telegram-bot
cd telegram-bot
```

Build the project run all tests
```terminaloutput
mvn clean verify
```

## Branches
Create a new branch from `develop` for your changes

Examples:

- `feature/webhook-support`
- `fix/connection-timeout`
- `docs/update-getting-started`
- `test/add-webhook-tests`

Do not make changes directly on `master` or `develop`.

## Making Changes

When working on a changes

1. Keep the changes focused and minimal
2. Follow the existing project structure and coding style.
3. Add or update tests when necessary.
4. Update documentation when your change effects public behavior
5. Make sure all tests pass before opening a Pull Request.

Run:
```terminaloutput
mvn clean verify
```

## Commit Messages

Use clear and descriptive commit messages.

Recommended format:

`type : description`

Examples:

feat:  add webhook support
fix: handle connection timeout
docs: update configuration guide
test: add webhook integration test
refactor: simplify authorization handling

## Pull Requests

Before opening a Pull Request:

- Make sure the project builds successfully
- Make sure all tests pass
- Add tests for new functionality or bug fixes where appropriate
- Keep the Pull Request focused on a single changes

Pull Requests should target the develop branch unless the repository maintainers specify otherwise.

All required CI checks and reviews must pass before a Pull Request can be merged.

## Issues

Use the appropriate GitHub Issue template when reporting a problem or requesting a feature.

When reporting a bug, include:

- A clear description of the problem
- Steps to reproduce it
- Expected behavior
- Actual behavior
- Relevant logs or stack traces
- Java and project version
   
## Code Style
Follow the existing coding conventions in the project.

### Avoid:
- Unnecessary changes
- Unrelated refactoring
- Adding unnecessary dependencies

### Tests

- New functionality should include appropriate  tests
- Bug fixes should include a regression test when practical
- Run the complete test suite before submitting a Pull Request:
    ```terminaloutput
    mvn clean verify
    ```
  
## Documentation

If a change affects public APIs, configuration, behavior, or usage, update the relevant documentation.

Documentation improvements are also welcome as standalone contributions.

## Questions

If you are unsure about an implementation or proposed change, open a GitHub Discussion or an Issue before starting significant work.

## License

By contributing to this project, you agree that your contributions will be licensed under the same license as the project.