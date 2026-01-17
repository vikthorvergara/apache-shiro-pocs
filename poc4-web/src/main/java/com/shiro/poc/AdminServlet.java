package com.shiro.poc;

import javax.servlet.http.*;
import org.apache.shiro.SecurityUtils;
import java.io.IOException;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        var out = resp.getWriter();
        var subject = SecurityUtils.getSubject();

        out.println("""
            <!DOCTYPE html><html><head><title>Admin Panel</title></head><body>
            <h1>Admin Panel</h1>
            <p>Only accessible to 'admin' role.</p>
            <p>User: %s | Admin: %s</p>
            <h2>Admin Actions</h2>
            <ul>
            <li>Manage Users</li>
            <li>View System Logs</li>
            <li>Configuration Settings</li>
            </ul>
            <p><a href='/dashboard'>Back to Dashboard</a></p>
            </body></html>
            """.formatted(subject.getPrincipal(), subject.hasRole("admin")));
    }
}
