-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS rbac_system;


-- 使用数据库
USE rbac_system;
-- 删除表（如果存在），以确保脚本可重复执行
DROP TABLE IF EXISTS menu_permissions;
DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS menus;
DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

-- 创建用户表 (users)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    status ENUM('enabled', 'disabled') NOT NULL DEFAULT 'enabled'
);

-- 创建角色表 (roles)
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- 创建权限表 (permissions)
CREATE TABLE permissions (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    module VARCHAR(50),
    method VARCHAR(50)
);

-- 创建用户-角色关联表 (user_roles)
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- 创建角色-权限关联表 (role_permissions)
CREATE TABLE role_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

-- 创建菜单表 (menus)
CREATE TABLE menus (
    menu_id INT AUTO_INCREMENT PRIMARY KEY,
    menu_name VARCHAR(100) NOT NULL,
    parent_id INT DEFAULT NULL,
    permission_id INT DEFAULT NULL,
    url VARCHAR(255),
    `order` INT DEFAULT 0,
    FOREIGN KEY (parent_id) REFERENCES menus(menu_id) ON DELETE SET NULL,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE SET NULL
);

-- 创建菜单-权限关联表 (menu_permissions)
CREATE TABLE menu_permissions (
    menu_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (menu_id, permission_id),
    FOREIGN KEY (menu_id) REFERENCES menus(menu_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

-- 插入测试数据到用户表 (users)
INSERT INTO users (username, password, email, status) VALUES
('admin', 'admin123', 'admin@example.com', 'enabled'),
('user1', 'password1', 'user1@example.com', 'enabled'),
('user2', 'password2', 'user2@example.com', 'disabled');

-- 插入测试数据到角色表 (roles)
INSERT INTO roles (role_name, description) VALUES
('Administrator', 'System administrator with full access'),
('Editor', 'Can edit content'),
('Viewer', 'Can view content');

-- 插入测试数据到权限表 (permissions)
INSERT INTO permissions (permission_name, description, module, method) VALUES
('view_dashboard', 'Access to view the dashboard', 'Dashboard', 'view'),
('edit_content', 'Permission to edit content', 'Content', 'edit'),
('delete_user', 'Permission to delete users', 'User Management', 'deleteUser');

-- 插入测试数据到用户-角色关联表 (user_roles)
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin is Administrator
(2, 2), -- user1 is Editor
(3, 3); -- user2 is Viewer

-- 插入测试数据到角色-权限关联表 (role_permissions)
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), -- Administrator has view_dashboard
(1, 2), -- Administrator has edit_content
(1, 3), -- Administrator has delete_user
(2, 1), -- Editor has view_dashboard
(2, 2), -- Editor has edit_content
(3, 1); -- Viewer has view_dashboard

-- 插入测试数据到菜单表 (menus)
INSERT INTO menus (menu_name, parent_id, permission_id, url, `order`) VALUES
('Dashboard', NULL, 1, '/dashboard', 1),
('Content Management', NULL, NULL, '/content', 2),
('Edit Content', 2, 2, '/content/edit', 1),
('User Management', NULL, NULL, '/users', 3),
('Delete User', 4, 3, '/users/delete', 1);

-- 插入测试数据到菜单-权限关联表 (menu_permissions)
INSERT INTO menu_permissions (menu_id, permission_id) VALUES
(1, 1), -- Dashboard menu requires view_dashboard permission
(3, 2), -- Edit Content menu requires edit_content permission
(5, 3); -- Delete User menu requires delete_user permission
