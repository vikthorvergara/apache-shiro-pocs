package com.shiro.poc;

import static java.lang.System.out;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.*;

public class HashedCredentialsRealm extends AuthorizingRealm {

    private final Map<String, UserCredentials> users = new HashMap<>();

    record UserCredentials(String hashedPassword, String salt, Set<String> roles) {}

    public HashedCredentialsRealm() {
        setName("HashedCredentialsRealm");

        var matcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
        matcher.setHashIterations(500000);
        matcher.setStoredCredentialsHexEncoded(true);
        setCredentialsMatcher(matcher);
    }

    public void addUser(String username, String plainPassword, String... roles) {
        var salt = username + System.currentTimeMillis();
        var hash = new Sha256Hash(plainPassword, salt, 500000);
        var hashedPassword = hash.toHex();

        users.put(username, new UserCredentials(hashedPassword, salt, Set.of(roles)));
        out.println("Added: " + username);
        out.println("  Salt: " + salt);
        out.println("  Hash: " + hashedPassword);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        var username = (String) token.getPrincipal();
        var creds = users.get(username);

        if (creds == null) {
            throw new UnknownAccountException("No account: " + username);
        }

        return new SimpleAuthenticationInfo(
            username,
            creds.hashedPassword(),
            ByteSource.Util.bytes(creds.salt()),
            getName()
        );
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        var username = (String) principals.getPrimaryPrincipal();
        var creds = users.get(username);

        var info = new SimpleAuthorizationInfo();
        if (creds != null) {
            info.setRoles(creds.roles());
        }
        return info;
    }
}
