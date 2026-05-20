# Project Coding Guidelines

This document is the English version of `RULE.md` and is intended for future contributors to this project.

## 1. Code Style

### 1.1 Naming Conventions
- Class names: Use PascalCase, for example `UserService`
- Method names and variable names: Use camelCase, for example `getUserInfo`
- Constants: Use SCREAMING_SNAKE_CASE, for example `MAX_CONNECTION_COUNT`
- Package names: Use lowercase letters only, with words separated by dots, for example `com.example.project.module`

### 1.2 Formatting Rules
- Indentation: Use 4 spaces; do not use tabs
- Line length: Keep each line within 120 characters
- Blank lines: Use blank lines to separate classes and methods
- Spacing: Add spaces around operators and after keywords where appropriate

## 2. Comment Guidelines

### 2.1 Class Comments
```java
/**
 * Description of the class responsibility
 * @author Author
 * @date Creation date
 */
```

### 2.2 Method Comments
```java
/**
 * Description of the method behavior
 * @param parameterName Description of the parameter
 * @return Description of the return value
 * @throws ExceptionType Description of the exception
 */
```

### 2.3 Inline Comments
- Use `//` for inline comments
- Keep comments concise and easy to understand
- Use comments to explain non-obvious or complex logic instead of restating the code

## 3. Code Structure

### 3.1 Class Layout
- Organize class members in the following order:
  member variables -> constructors -> public methods -> private methods
- Group related methods by feature or responsibility

### 3.2 Method Size
- A single method should not exceed 50 lines whenever possible
- Break complex logic into smaller, focused helper methods

## 4. Error Handling

### 4.1 Exception Handling
- Handle exceptions explicitly and avoid swallowing them silently
- Provide clear and actionable error messages
- Use specific exception types whenever possible instead of overly broad catches

## 5. Testing Guidelines

### 5.1 Unit Tests
- Each public method should have a corresponding unit test
- Test cases should cover both normal scenarios and exceptional scenarios
- Use meaningful and descriptive test method names

## 6. Commit Guidelines

### 6.1 Git Commit Message Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation update
- `style`: Code formatting changes
- `refactor`: Refactoring
- `test`: Test-related changes
- `chore`: Changes to the build process or supporting tools
