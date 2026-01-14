package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;

public class BasicAuthApp {

    public static void main(String[] args) {
        var realm = new IniRealm("classpath:shiro.ini");
        var securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

        out.println("=== Apache Shiro Basic Authentication POC ===\n");

        testLogin("admin", "root");
        testLogin("user1", "pwd1");
        testLogin("user1", "wrongpassword");
        testLogin("unknown", "password");

        demonstrateSession();
    }

    static void testLogin(String username, String password) {
        var subject = SecurityUtils.getSubject();
        var token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            out.println("[SUCCESS] " + username);
            out.println("  Principal: " + subject.getPrincipal());
            out.println("  Authenticated: " + subject.isAuthenticated());
            out.println("  admin role: " + subject.hasRole("admin"));
            out.println("  user role: " + subject.hasRole("user"));
            subject.logout();
            out.println("  Logged out\n");
        } catch (UnknownAccountException _) {
            out.println("[FAILED] Unknown account: " + username + "\n");
        } catch (IncorrectCredentialsException _) {
            out.println("[FAILED] Wrong password: " + username + "\n");
        } catch (AuthenticationException e) {
            out.println("[FAILED] Auth error: " + username + " - " + e.getMessage() + "\n");
        }
    }

    static void demonstrateSession() {
        out.println("=== Session Demo ===\n");

        var subject = SecurityUtils.getSubject();
        var token = new UsernamePasswordToken("user2", "pwd2");
        token.setRememberMe(true);

        subject.login(token);
        var session = subject.getSession();

        out.println("User: " + subject.getPrincipal());
        out.println("Session ID: " + session.getId());
        out.println("Remember Me: " + subject.isRemembered());

        session.setAttribute("key", "value");
        out.println("Session attr: " + session.getAttribute("key"));

        subject.logout();
        out.println("After logout: " + subject.isAuthenticated());
        out.println("\n=== POC Complete ===");
    }
}
