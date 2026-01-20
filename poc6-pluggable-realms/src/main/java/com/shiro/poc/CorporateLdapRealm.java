package com.shiro.poc;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;

public class CorporateLdapRealm extends AuthorizingRealm {

    private static final Map<String, String> CORPORATE_USERS = new HashMap<>();
    private static final Map<String, Set<String>> CORPORATE_ROLES = new HashMap<>();

    static {
        CORPORATE_USERS.put("andi", "corp123");
        CORPORATE_USERS.put("marley", "corp456");

        CORPORATE_ROLES.put("andi", Set.of("employee", "manager", "admin"));
        CORPORATE_ROLES.put("marley", Set.of("employee", "developer"));
    }

    public CorporateLdapRealm() {
        setName("CorporateLDAP");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = ((UsernamePasswordToken) token).getUsername();
        String password = CORPORATE_USERS.get(username);

        if (password == null) {
            return null;
        }

        System.out.println("    [CorporateLDAP] Found user: " + username);
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Set<String> roles = CORPORATE_ROLES.getOrDefault(username, Set.of());
        info.setRoles(roles);

        if (roles.contains("admin")) {
            info.addStringPermission("system:*");
        }
        if (roles.contains("manager")) {
            info.addStringPermission("reports:read");
            info.addStringPermission("team:manage");
        }
        if (roles.contains("developer")) {
            info.addStringPermission("code:read");
            info.addStringPermission("code:write");
        }

        return info;
    }
}
