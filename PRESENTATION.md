# *WIP*: Presentation

## 1. What (1 min)
- Java security framework
- Handles: Authentication, Authorization, Sessions, Cryptography
- Alternative to Spring Security (simpler, standalone)

## 2. How (1.5 min)
- Subject -> SecurityManager -> Realm -> Data Store
- Show architecture diagram
- Explain the delegation flow

## 3. Trade-offs (1 min)

| Pros | Cons |
|------|------|
| Simple API | Smaller community than Spring Security |
| Framework-agnostic | Less enterprise features out-of-box |
| Works in non-web apps | Manual config for advanced scenarios |
| Easy to learn | |

## 4. Code/Demo (1 min)
- Show POC #1: Basic login/logout
- Live demo: successful and failed authentication

## 5. Comparisons (0.5 min)

| Feature | Shiro | Spring Security |
|---------|-------|-----------------|
| Learning curve | Low | High |
| Standalone support | Yes | Limited |
| Web framework dependency | None | Spring |
| Community size | Medium | Large |

# Resources

- [Official Documentation](https://shiro.apache.org/documentation.html)
- [10-Minute Tutorial](https://shiro.apache.org/10-minute-tutorial.html)
- [Architecture](https://shiro.apache.org/architecture.html)
- [Web Support](https://shiro.apache.org/web.html)
- [GitHub Repository](https://github.com/apache/shiro)
