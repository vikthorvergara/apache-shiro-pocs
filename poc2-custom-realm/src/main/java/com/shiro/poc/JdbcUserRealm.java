package com.shiro.poc;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;

public class JdbcUserRealm extends AuthorizingRealm {

    private final DataSource dataSource;

    public JdbcUserRealm(DataSource dataSource) {
        this.dataSource = dataSource;
        setName("JdbcUserRealm");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        var username = ((UsernamePasswordToken) token).getUsername();
        var password = getPasswordForUser(username);

        if (password == null) {
            throw new UnknownAccountException("No account: " + username);
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        var username = (String) principals.getPrimaryPrincipal();
        var info = new SimpleAuthorizationInfo(getRolesForUser(username));
        info.setStringPermissions(getPermissionsForUser(username));
        return info;
    }

    String getPasswordForUser(String username) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            return rs.next() ? rs.getString("password") : null;
        } catch (SQLException e) {
            throw new AuthenticationException("Error querying user", e);
        }
    }

    HashSet<String> getRolesForUser(String username) {
        var roles = new HashSet<String>();
        var sql = """
            SELECT r.role_name FROM roles r
            JOIN user_roles ur ON r.id = ur.role_id
            JOIN users u ON u.id = ur.user_id
            WHERE u.username = ?""";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            while (rs.next()) roles.add(rs.getString("role_name"));
        } catch (SQLException e) {
            throw new RuntimeException("Error querying roles", e);
        }
        return roles;
    }

    HashSet<String> getPermissionsForUser(String username) {
        var permissions = new HashSet<String>();
        var sql = """
            SELECT p.permission FROM permissions p
            JOIN role_permissions rp ON p.id = rp.permission_id
            JOIN roles r ON r.id = rp.role_id
            JOIN user_roles ur ON r.id = ur.role_id
            JOIN users u ON u.id = ur.user_id
            WHERE u.username = ?""";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            while (rs.next()) permissions.add(rs.getString("permission"));
        } catch (SQLException e) {
            throw new RuntimeException("Error querying permissions", e);
        }
        return permissions;
    }
}
