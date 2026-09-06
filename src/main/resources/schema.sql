-- Smart Gated Community Management System Schema (PostgreSQL DDL)

-- Drop existing tables if re-creating
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS notices CASCADE;
DROP TABLE IF EXISTS complaints CASCADE;
DROP TABLE IF EXISTS maintenance_requests CASCADE;
DROP TABLE IF EXISTS visitors CASCADE;
DROP TABLE IF EXISTS residents CASCADE;
DROP TABLE IF EXISTS flats CASCADE;
DROP TABLE IF EXISTS buildings CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Roles Table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User-Roles Join Table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Buildings Table
CREATE TABLE buildings (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    total_floors INT NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Flats Table
CREATE TABLE flats (
    id BIGSERIAL PRIMARY KEY,
    building_id BIGINT NOT NULL REFERENCES buildings(id) ON DELETE CASCADE,
    flat_number VARCHAR(20) NOT NULL,
    floor_number INT NOT NULL,
    status VARCHAR(30) DEFAULT 'VACANT',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_building_flat UNIQUE (building_id, flat_number)
);

-- Residents Table
CREATE TABLE residents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    flat_id BIGINT NOT NULL REFERENCES flats(id) ON DELETE CASCADE,
    resident_type VARCHAR(30) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    move_in_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Visitors Table
CREATE TABLE visitors (
    id BIGSERIAL PRIMARY KEY,
    resident_id BIGINT NOT NULL REFERENCES residents(id) ON DELETE CASCADE,
    flat_id BIGINT NOT NULL REFERENCES flats(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    vehicle_number VARCHAR(30),
    purpose VARCHAR(255) NOT NULL,
    visitor_type VARCHAR(30) NOT NULL,
    expected_arrival TIMESTAMP WITH TIME ZONE NOT NULL,
    entry_time TIMESTAMP WITH TIME ZONE,
    exit_time TIMESTAMP WITH TIME ZONE,
    status VARCHAR(30) DEFAULT 'PRE_REGISTERED',
    access_code VARCHAR(50) NOT NULL UNIQUE,
    verified_by_guard_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Maintenance Requests Table
CREATE TABLE maintenance_requests (
    id BIGSERIAL PRIMARY KEY,
    resident_id BIGINT NOT NULL REFERENCES residents(id) ON DELETE CASCADE,
    flat_id BIGINT NOT NULL REFERENCES flats(id) ON DELETE CASCADE,
    assigned_staff_id BIGINT REFERENCES users(id),
    category VARCHAR(50) NOT NULL,
    priority VARCHAR(30) DEFAULT 'MEDIUM',
    status VARCHAR(30) DEFAULT 'PENDING',
    description TEXT NOT NULL,
    image_url VARCHAR(255),
    completion_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE
);

-- Complaints Table
CREATE TABLE complaints (
    id BIGSERIAL PRIMARY KEY,
    resident_id BIGINT NOT NULL REFERENCES residents(id) ON DELETE CASCADE,
    flat_id BIGINT NOT NULL REFERENCES flats(id) ON DELETE CASCADE,
    assigned_staff_id BIGINT REFERENCES users(id),
    title VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    status VARCHAR(30) DEFAULT 'PENDING',
    description TEXT NOT NULL,
    resolution_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE
);

-- Notices Table
CREATE TABLE notices (
    id BIGSERIAL PRIMARY KEY,
    created_by_admin_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    target_audience VARCHAR(50) DEFAULT 'ALL',
    is_pinned BOOLEAN DEFAULT FALSE,
    publish_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Payments Table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    resident_id BIGINT NOT NULL REFERENCES residents(id) ON DELETE CASCADE,
    flat_id BIGINT NOT NULL REFERENCES flats(id) ON DELETE CASCADE,
    amount DECIMAL(10, 2) NOT NULL,
    fee_type VARCHAR(50) NOT NULL,
    payment_status VARCHAR(30) DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    transaction_ref VARCHAR(100) UNIQUE,
    due_date DATE NOT NULL,
    paid_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Notifications Table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    target_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for Query Performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_flats_building ON flats(building_id);
CREATE INDEX idx_residents_user ON residents(user_id);
CREATE INDEX idx_residents_flat ON residents(flat_id);
CREATE INDEX idx_visitors_access_code ON visitors(access_code);
CREATE INDEX idx_visitors_status ON visitors(status);
CREATE INDEX idx_visitors_expected ON visitors(expected_arrival);
CREATE INDEX idx_maint_status ON maintenance_requests(status);
CREATE INDEX idx_maint_assigned ON maintenance_requests(assigned_staff_id);
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_notices_pinned ON notices(is_pinned, publish_date);
CREATE INDEX idx_payments_resident ON payments(resident_id);
CREATE INDEX idx_notifications_user ON notifications(user_id, is_read);
