package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;

public class DocumentService {

    public String readDocument(String docId) {
        checkPermission("document:read");
        return "Content: " + docId;
    }

    public void writeDocument(String docId, String content) {
        checkPermission("document:write");
        out.println("  Written: " + docId);
    }

    public void createDocument(String docId) {
        checkPermission("document:create");
        out.println("  Created: " + docId);
    }

    public void deleteDocument(String docId) {
        checkPermission("document:delete");
        out.println("  Deleted: " + docId);
    }

    public String readReport(String reportId) {
        checkPermission("report:read");
        return "Report: " + reportId;
    }

    void checkPermission(String permission) {
        var subject = SecurityUtils.getSubject();
        if (!subject.isPermitted(permission)) {
            throw new UnauthorizedException("No permission: " + permission);
        }
    }
}
