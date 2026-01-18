## POC 5: Password Hashing

Demonstrates secure password storage using hashing, salting, and key stretching.

**Key takeaways:**
- Never store plaintext passwords
- Salting prevents rainbow table attacks (same password = different hashes)
- Hash iterations (key stretching) slow down brute force attacks
- `HashedCredentialsMatcher` verifies hashed passwords during login
- SHA-256 with 500k+ iterations is recommended for production

**Run:** `mvn compile exec:java`
