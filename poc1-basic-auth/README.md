# POC 1: Basic Authentication

Demonstrates the fundamental Shiro authentication flow using an INI file as the user data source.

**Key takeaways:**
- `SecurityUtils.getSubject()` is the entry point for all security operations
- `UsernamePasswordToken` wraps user credentials for authentication
- `subject.login(token)` triggers the authentication process
- Different exceptions indicate different failure reasons (UnknownAccountException, IncorrectCredentialsException)
- Sessions are managed independently from the container

**Run:** `mvn compile exec:java`
