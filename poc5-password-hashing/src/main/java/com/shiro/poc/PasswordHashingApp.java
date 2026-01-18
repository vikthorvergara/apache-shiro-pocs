package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.mgt.DefaultSecurityManager;

public class PasswordHashingApp {

    public static void main(String[] args) {
        out.println("=== Apache Shiro Password Hashing POC ===\n");

        demonstrateHashAlgorithms();
        demonstrateSalting();
        demonstrateIterations();
        demonstrateSecureAuthentication();

        out.println("=== POC Complete ===");
    }

    static void demonstrateHashAlgorithms() {
        out.println("--- Hash Algorithms ---");
        var password = "mySecretPassword";

        var md5 = new Md5Hash(password).toHex();
        var sha256 = new Sha256Hash(password).toHex();
        var sha512 = new Sha512Hash(password).toHex();

        out.println("Original: " + password);
        out.println("MD5:      " + md5 + " (32 chars)");
        out.println("SHA-256:  " + sha256 + " (64 chars)");
        out.println("SHA-512:  " + sha512.substring(0, 64) + "... (128 chars)\n");
    }

    static void demonstrateSalting() {
        out.println("--- Salting ---");
        var password = "password123";
        var rng = new SecureRandomNumberGenerator();

        var salt1 = rng.nextBytes();
        var salt2 = rng.nextBytes();

        var hash1 = new Sha256Hash(password, salt1).toHex();
        var hash2 = new Sha256Hash(password, salt2).toHex();
        var hashNoSalt = new Sha256Hash(password).toHex();

        out.println("Same password, different salts:");
        out.println("  No salt: " + hashNoSalt);
        out.println("  Salt 1:  " + hash1);
        out.println("  Salt 2:  " + hash2);
        out.println("  Match:   " + hash1.equals(hash2) + "\n");
    }

    static void demonstrateIterations() {
        out.println("--- Hash Iterations (Key Stretching) ---");
        var password = "password";
        var salt = "randomSalt";

        var result1 = timeHash(password, salt, 1);
        var result1k = timeHash(password, salt, 1000);
        var result500k = timeHash(password, salt, 500000);

        out.println("1 iter:      " + result1.hash.substring(0, 32) + "... (" + result1.ms + "ms)");
        out.println("1,000 iter:  " + result1k.hash.substring(0, 32) + "... (" + result1k.ms + "ms)");
        out.println("500,000 iter:" + result500k.hash.substring(0, 32) + "... (" + result500k.ms + "ms)");
        out.println("More iterations = slower but more secure\n");
    }

    static HashResult timeHash(String password, String salt, int iterations) {
        var start = System.currentTimeMillis();
        var hash = new Sha256Hash(password, salt, iterations).toHex();
        return new HashResult(hash, System.currentTimeMillis() - start);
    }

    record HashResult(String hash, long ms) {}

    static void demonstrateSecureAuthentication() {
        out.println("--- Secure Authentication with Hashed Credentials ---\n");

        var realm = new HashedCredentialsRealm();
        realm.addUser("alice", "alicePass", "admin");
        realm.addUser("bob", "bobPass", "user");
        out.println();

        var securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

        testLogin("alice", "alicePass");
        testLogin("alice", "wrongPassword");
        testLogin("bob", "bobPass");
        testLogin("unknown", "password");
    }

    static void testLogin(String username, String password) {
        var subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(username, password));
            out.println("[SUCCESS] " + username + " | admin=" + subject.hasRole("admin") + ", user=" + subject.hasRole("user"));
            subject.logout();
        } catch (AuthenticationException e) {
            out.println("[FAILED] " + username + ": " + e.getClass().getSimpleName());
        }
    }
}
