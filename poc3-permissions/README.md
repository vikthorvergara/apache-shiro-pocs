# POC 3: Permission-Based Authorization

Demonstrates fine-grained permission checking using Shiro's WildcardPermission system.

**Key takeaways:**
- Permissions follow pattern: `resource:action:instance` (e.g., `document:read:doc123`)
- Wildcards (`*`) imply all actions/instances
- `subject.isPermitted()` checks if action is allowed
- `subject.checkPermission()` throws exception if not permitted
- Permissions are more flexible than roles for access control

**Run:** `mvn compile exec:java`
