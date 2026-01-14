# Apache Shiro POCs

## What is Apache Shiro?

Apache Shiro is a Java security framework that handles authentication, authorization, cryptography, and session management.

## Core Concepts

### Subject
- Represents the "current user" (human, process, or service)
- Entry point for all security operations
- Access via `SecurityUtils.getSubject()`

### Authentication
- Identity verification (login/logout)
- Uses AuthenticationToken and Credentials
- Supports Remember Me functionality

### Authorization
- Access control (what users can do)
- Roles: coarse-grained grouping (`@RequiresRoles`, `subject.hasRole()`)
- Permissions: fine-grained control (`@RequiresPermissions`, `subject.isPermitted()`)

### Realm
- Bridge between Shiro and your security data (DB, LDAP, file)
- Handles authentication and authorization lookups
- Supports multiple realms per application

### SecurityManager
- Heart of Shiro's architecture
- Coordinates all security components
- Configured via INI file or programmatically

### Session Management
- Container-independent sessions
- Works in non-web environments (CLI, microservices)
- Supports session clustering

### Cryptography
- Password hashing (SHA-256, BCrypt)
- Encryption/Decryption utilities
- Built-in codecs (Base64, Hex)

## Architecture

```
+----------------+
|    Subject     |  <-- Your code interacts here
+----------------+
        |
        v
+----------------+
| SecurityManager|  <-- Coordinates everything
+----------------+
        |
        v
+----------------+
|     Realm      |  <-- Connects to your data
+----------------+
        |
        v
+----------------+
|   Data Store   |  <-- DB, LDAP, INI file
+----------------+
```

Subject delegates to SecurityManager, which delegates to one or more Realms to verify credentials and fetch roles/permissions.

## POC List

1. **Basic Authentication**: Subject, AuthenticationToken, INI Realm
  - INI config, login/logout flow

2. **Custom Realm**: Realm interface, AuthorizingRealm
  - JDBC realm with user/role tables

3. **Permission-Based Authorization**: WildcardPermission, @RequiresPermissions
  - Fine-grained permission checks

4. **Web Integration**: ShiroFilter, web.xml config
  - Servlet filters, session management

5. **Password Hashing**: HashedCredentialsMatcher, Salting
  - Secure credential storage


## Running the POCs

```bash
cd pocX-name
mvn compile exec:java
```
