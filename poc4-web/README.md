# POC 4: Web Integration

Shows Shiro integration with a web application using servlet filters and URL-based security.

**Key takeaways:**
- ShiroFilter intercepts all requests and applies security rules
- URL patterns map to filter chains (`/admin/** = authc, roles[admin]`)
- `anon` filter allows anonymous access
- `authc` filter requires authentication
- `roles[X]` filter requires specific role
- Sessions work across requests automatically

**Run:** `mvn compile exec:java` then open http://localhost:8080
