package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;

public class CustomRealmApp {

    public static void main(String[] args) throws Exception {
        out.println("=== Apache Shiro Custom JDBC Realm POC ===\n");

        var dataSource = DatabaseSetup.createDataSource();
        DatabaseSetup.initializeSchema(dataSource);
        out.println("[DB] H2 initialized\n");

        var realm = new JdbcUserRealm(dataSource);
        var securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

        testUser("admin", "admin123");
        testUser("developer", "dev123");
        testUser("viewer", "view123");

        out.println("=== POC Complete ===");
    }

    static void testUser(String username, String password) {
        var subject = SecurityUtils.getSubject();

        out.println("--- " + username + " ---");
        subject.login(new UsernamePasswordToken(username, password));

        out.println("Roles: admin=" + subject.hasRole("admin") +
                    ", developer=" + subject.hasRole("developer") +
                    ", viewer=" + subject.hasRole("viewer"));

        out.println("Permissions:");
        out.println("  user:*         = " + subject.isPermitted("user:*"));
        out.println("  project:read   = " + subject.isPermitted("project:read"));
        out.println("  project:write  = " + subject.isPermitted("project:write"));
        out.println("  project:delete = " + subject.isPermitted("project:delete"));
        out.println("  report:read    = " + subject.isPermitted("report:read"));

        subject.logout();
        out.println();
    }
}
