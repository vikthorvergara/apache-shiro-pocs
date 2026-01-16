package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;

public class PermissionsApp {

    public static void main(String[] args) {
        out.println("=== Apache Shiro Permission-Based Authorization POC ===\n");

        var realm = new IniRealm("classpath:shiro.ini");
        var securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

        var service = new DocumentService();

        demonstrateWildcardPermissions();

        out.println("\n--- admin ---");
        testUserOperations("admin", "admin123", service);

        out.println("\n--- editor ---");
        testUserOperations("editor", "editor123", service);

        out.println("\n--- viewer ---");
        testUserOperations("viewer", "viewer123", service);

        out.println("\n=== POC Complete ===");
    }

    static void demonstrateWildcardPermissions() {
        out.println("--- Wildcard Permission Matching ---");

        var allDocs = new WildcardPermission("document:*");
        var readDoc = new WildcardPermission("document:read");
        var writeDoc = new WildcardPermission("document:write");

        out.println("'document:*' implies 'document:read': " + allDocs.implies(readDoc));
        out.println("'document:*' implies 'document:write': " + allDocs.implies(writeDoc));
        out.println("'document:read' implies 'document:*': " + readDoc.implies(allDocs));

        var specificDoc = new WildcardPermission("document:read:doc123");
        var anyDocRead = new WildcardPermission("document:read:*");
        out.println("'document:read:*' implies 'document:read:doc123': " + anyDocRead.implies(specificDoc));

        var everything = new WildcardPermission("*");
        out.println("'*' implies 'document:read': " + everything.implies(readDoc));
    }

    static void testUserOperations(String username, String password, DocumentService service) {
        var subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password));

        out.println("Permissions:");
        out.println("  document:read   = " + subject.isPermitted("document:read"));
        out.println("  document:write  = " + subject.isPermitted("document:write"));
        out.println("  document:create = " + subject.isPermitted("document:create"));
        out.println("  document:delete = " + subject.isPermitted("document:delete"));
        out.println("  document:*      = " + subject.isPermitted("document:*"));

        var results = subject.isPermitted("document:read", "document:write", "report:read");
        out.println("Batch [read,write,report]: " + results[0] + "," + results[1] + "," + results[2]);

        out.println("Operations:");
        tryOp("Read", () -> out.println("  " + service.readDocument("doc-001")));
        tryOp("Write", () -> service.writeDocument("doc-001", "content"));
        tryOp("Create", () -> service.createDocument("doc-002"));
        tryOp("Delete", () -> service.deleteDocument("doc-001"));
        tryOp("Report", () -> out.println("  " + service.readReport("report-001")));

        subject.logout();
    }

    static void tryOp(String name, Runnable op) {
        try {
            op.run();
        } catch (UnauthorizedException _) {
            out.println("  [DENIED] " + name);
        }
    }
}
