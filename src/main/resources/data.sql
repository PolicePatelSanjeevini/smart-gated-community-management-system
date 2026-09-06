-- Default System Roles
INSERT INTO roles (id, name, description) VALUES
(1, 'ROLE_ADMIN', 'Community Administrator with full system management access'),
(2, 'ROLE_RESIDENT', 'Flat Resident with visitor, complaint, and maintenance access'),
(3, 'ROLE_SECURITY_GUARD', 'Security Guard with entry/exit and visitor verification access'),
(4, 'ROLE_MAINTENANCE_STAFF', 'Maintenance Personnel assigned to community tasks')
ON CONFLICT (name) DO NOTHING;

-- Initial System Users (Passwords are BCrypt hashed for 'Admin@123', 'Resident@123', 'Guard@123', 'Staff@123')
-- BCrypt encoded passwords:
-- Admin@123 -> $2a$10$4y9p0t4B3u3K5b7F9t1E.e/0n2Z6k1x8Y.a2b3c4d5e6f7g8h9i0
-- (System startup DataInitializer will ensure BCrypt hashes are correctly created and assigned if not exists)
