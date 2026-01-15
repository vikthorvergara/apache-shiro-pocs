package com.shiro.poc;

import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;

public class DatabaseSetup {

    public static DataSource createDataSource() {
        var ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:shiro;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    public static void initializeSchema(DataSource dataSource) throws Exception {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL)""");

            stmt.execute("""
                CREATE TABLE roles (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    role_name VARCHAR(50) UNIQUE NOT NULL)""");

            stmt.execute("""
                CREATE TABLE permissions (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    permission VARCHAR(100) UNIQUE NOT NULL)""");

            stmt.execute("""
                CREATE TABLE user_roles (
                    user_id INT,
                    role_id INT,
                    PRIMARY KEY (user_id, role_id),
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (role_id) REFERENCES roles(id))""");

            stmt.execute("""
                CREATE TABLE role_permissions (
                    role_id INT,
                    permission_id INT,
                    PRIMARY KEY (role_id, permission_id),
                    FOREIGN KEY (role_id) REFERENCES roles(id),
                    FOREIGN KEY (permission_id) REFERENCES permissions(id))""");

            stmt.execute("INSERT INTO users (username, password) VALUES ('admin', 'admin123')");
            stmt.execute("INSERT INTO users (username, password) VALUES ('developer', 'dev123')");
            stmt.execute("INSERT INTO users (username, password) VALUES ('viewer', 'view123')");

            stmt.execute("INSERT INTO roles (role_name) VALUES ('admin')");
            stmt.execute("INSERT INTO roles (role_name) VALUES ('developer')");
            stmt.execute("INSERT INTO roles (role_name) VALUES ('viewer')");

            stmt.execute("INSERT INTO permissions (permission) VALUES ('user:*')");
            stmt.execute("INSERT INTO permissions (permission) VALUES ('project:read')");
            stmt.execute("INSERT INTO permissions (permission) VALUES ('project:write')");
            stmt.execute("INSERT INTO permissions (permission) VALUES ('project:delete')");
            stmt.execute("INSERT INTO permissions (permission) VALUES ('report:read')");

            stmt.execute("INSERT INTO user_roles (user_id, role_id) VALUES (1, 1)");
            stmt.execute("INSERT INTO user_roles (user_id, role_id) VALUES (2, 2)");
            stmt.execute("INSERT INTO user_roles (user_id, role_id) VALUES (3, 3)");

            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 3)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 4)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 5)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 2)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 3)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 5)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 2)");
            stmt.execute("INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 5)");
        }
    }
}
