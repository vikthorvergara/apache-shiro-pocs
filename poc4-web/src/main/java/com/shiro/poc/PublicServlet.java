package com.shiro.poc;

import javax.servlet.http.*;
import org.apache.shiro.SecurityUtils;
import java.io.IOException;

public class PublicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        var out = resp.getWriter();
        var subject = SecurityUtils.getSubject();

        var authSection = subject.isAuthenticated()
            ? "<p>Logged in as: %s</p><p><a href='/dashboard'>Dashboard</a></p>".formatted(subject.getPrincipal())
            : "<p>Not logged in.</p><p><a href='/login'>Login</a></p>";

        out.println("""
            <!DOCTYPE html><html><head><title>Public Page</title></head><body>
            <h1>Public Information</h1>
            <p>Accessible to everyone (no auth required).</p>
            <p>Filter: /public/** = anon</p>
            %s
            </body></html>
            """.formatted(authSection));
    }
}
