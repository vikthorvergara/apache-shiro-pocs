package com.shiro.poc;

import javax.servlet.http.*;
import org.apache.shiro.SecurityUtils;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        var out = resp.getWriter();
        var subject = SecurityUtils.getSubject();

        var adminLink = subject.hasRole("admin")
            ? "<li><a href='/admin/panel'>Admin Panel</a></li>" : "";

        out.println("""
            <!DOCTYPE html><html><head><title>Dashboard</title></head><body>
            <h1>Dashboard</h1>
            <p>Welcome, <strong>%s</strong></p>
            <p>Session: %s</p>
            <p>Authenticated: %s | Remembered: %s</p>
            <h2>Roles</h2>
            <ul><li>admin: %s</li><li>user: %s</li></ul>
            <h2>Navigation</h2>
            <ul>
            <li><a href='/public/info'>Public Page</a></li>
            %s
            <li><a href='/api/status'>API Status</a></li>
            <li><a href='/logout'>Logout</a></li>
            </ul>
            </body></html>
            """.formatted(
                subject.getPrincipal(),
                subject.getSession().getId(),
                subject.isAuthenticated(),
                subject.isRemembered(),
                subject.hasRole("admin"),
                subject.hasRole("user"),
                adminLink
            ));
    }
}
