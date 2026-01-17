package com.shiro.poc;

import javax.servlet.http.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        var out = resp.getWriter();
        var error = req.getParameter("error");

        out.println("""
            <!DOCTYPE html><html><head><title>Login</title></head><body>
            <h1>Shiro Web Login</h1>
            %s
            <form method='post'>
            <label>Username: <input type='text' name='username'></label><br><br>
            <label>Password: <input type='password' name='password'></label><br><br>
            <label><input type='checkbox' name='rememberMe'> Remember Me</label><br><br>
            <button type='submit'>Login</button>
            </form>
            <p>Users: admin/admin123, user/user123</p>
            </body></html>
            """.formatted(error != null ? "<p style='color:red'>Invalid credentials</p>" : ""));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var username = req.getParameter("username");
        var password = req.getParameter("password");
        var rememberMe = "on".equals(req.getParameter("rememberMe"));

        var subject = SecurityUtils.getSubject();
        var token = new UsernamePasswordToken(username, password, rememberMe);

        try {
            subject.login(token);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (AuthenticationException _) {
            resp.sendRedirect(req.getContextPath() + "/login?error=1");
        }
    }
}
