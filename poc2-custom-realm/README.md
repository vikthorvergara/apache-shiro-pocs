# POC 2: Custom Realm

Shows how to create a custom JDBC realm that connects Shiro to a relational database (H2).

**Key takeaways:**
- Extend `AuthorizingRealm` to create custom authentication/authorization logic
- `doGetAuthenticationInfo()` handles credential lookup
- `doGetAuthorizationInfo()` handles role/permission lookup
- Realms are the bridge between Shiro and your data source
- Multiple realms can be configured for different data sources

**Run:** `mvn compile exec:java`
