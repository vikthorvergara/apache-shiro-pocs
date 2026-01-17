package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class WebApp {

    public static void main(String[] args) throws Exception {
        out.println("=== Apache Shiro Web Integration POC ===\n");

        var server = new Server(8080);
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.setInitParameter("shiroConfigLocations", "classpath:shiro.ini");
        context.addEventListener(new EnvironmentLoaderListener());

        var shiroFilter = new FilterHolder(ShiroFilter.class);
        context.addFilter(shiroFilter, "/*", EnumSet.allOf(DispatcherType.class));

        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new DashboardServlet()), "/dashboard");
        context.addServlet(new ServletHolder(new AdminServlet()), "/admin/*");
        context.addServlet(new ServletHolder(new PublicServlet()), "/public/*");
        context.addServlet(new ServletHolder(new ApiServlet()), "/api/*");

        server.setHandler(context);

        out.println("""
            Server: http://localhost:8080

            URL Mappings:
              /login       -> anon
              /public/**   -> anon
              /admin/**    -> authc, roles[admin]
              /dashboard   -> authc
              /api/**      -> authc
              /logout      -> logout

            Users:
              admin/admin123 (admin)
              user/user123 (user)

            Ctrl+C to stop.
            """);

        server.start();
        server.join();
    }
}
