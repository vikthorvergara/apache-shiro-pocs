package com.shiro.poc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

import java.util.*;

public class PluggableRealmsApp {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     APACHE SHIRO - PLUGGABLE AUTHENTICATION DEMO     ║");
        System.out.println("║     Demonstrating Multi-Realm Flexibility            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        System.out.println("PHASE 1: Plugging in authentication sources\n");

        CorporateLdapRealm corporateRealm = new CorporateLdapRealm();
        System.out.println("Plugged in: CorporateLDAP realm (employees: andi, marley)");

        IniRealm partnerRealm = new IniRealm("classpath:shiro-partners.ini");
        partnerRealm.setName("PartnerFile");
        System.out.println("Plugged in: PartnerFile realm (partners: valve_gabe)");

        System.out.println("\nPHASE 2: Configuring authentication strategy\n");

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        List<Realm> realms = Arrays.asList(corporateRealm, partnerRealm);
        securityManager.setRealms(realms);

        ModularRealmAuthenticator authenticator = (ModularRealmAuthenticator) securityManager.getAuthenticator();
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());

        System.out.println("Strategy: FirstSuccessfulStrategy");
        System.out.println("First realm to authenticate the user wins\n");

        SecurityUtils.setSecurityManager(securityManager);

        System.out.println("PHASE 3: Testing authentication across realms\n");
        System.out.println("─".repeat(60));

        String[][] testUsers = {
            {"andi", "corp123", "Corporate employee"},
            {"valve_gabe", "valve2024", "External partner (Valve)"},
            {"marley", "corp456", "Corporate developer"},
            {"unknown", "test", "Non-existent user"}
        };

        for (String[] user : testUsers) {
            testAuthentication(user[0], user[1], user[2]);
        }

        System.out.println("\nPHASE 4: Hot-swapping authentication strategy\n");
        System.out.println("Switching to: AtLeastOneSuccessfulStrategy");
        System.out.println("Tries ALL realms, requires at least one success\n");

        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

        System.out.println("─".repeat(60));
        testAuthentication("andi", "corp123", "Re-testing with new strategy");

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  KEY TAKEAWAY: Full Pluggability                             ║");
        System.out.println("║  • Add/remove realms without code changes                    ║");
        System.out.println("║  • Swap strategies at runtime                                ║");
        System.out.println("║  • Same API for all identity providers                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private static void testAuthentication(String username, String password, String description) {
        System.out.println("\nTesting: " + description);
        System.out.println("User: " + username);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            System.out.println("Result: AUTHENTICATED");

            if (subject.hasRole("admin")) System.out.println("  > Has role: admin");
            if (subject.hasRole("employee")) System.out.println("  > Has role: employee");
            if (subject.hasRole("partner")) System.out.println("  > Has role: partner");
            if (subject.hasRole("developer")) System.out.println("  > Has role: developer");

            subject.logout();
        } catch (UnknownAccountException e) {
            System.out.println("  Result: User not found in any realm");
        } catch (IncorrectCredentialsException e) {
            System.out.println("  Result: Wrong password");
        } catch (AuthenticationException e) {
            System.out.println("  Result: Not found in any realm");
        }

        System.out.println("─".repeat(60));
    }
}
