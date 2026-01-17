package com.shiro.poc;

import javax.servlet.http.*;
import org.apache.shiro.SecurityUtils;
import java.io.IOException;

public class ApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        var out = resp.getWriter();
        var subject = SecurityUtils.getSubject();

        out.println("""
            {
              "status": "ok",
              "user": "%s",
              "authenticated": %s,
              "roles": { "admin": %s, "user": %s },
              "sessionId": "%s"
            }
            """.formatted(
                subject.getPrincipal(),
                subject.isAuthenticated(),
                subject.hasRole("admin"),
                subject.hasRole("user"),
                subject.getSession().getId()
            ));
    }
}
