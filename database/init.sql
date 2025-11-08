-- Create database
CREATE DATABASE IF NOT EXISTS smart_city;
USE smart_city;

-- Create user for application
CREATE USER IF NOT EXISTS 'cityuser'@'%' IDENTIFIED BY 'citypass';
GRANT ALL PRIVILEGES ON smart_city.* TO 'cityuser'@'%';
FLUSH PRIVILEGES;

-- City operations table
CREATE TABLE IF NOT EXISTS city_operations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT,
    severity INT NOT NULL,
    timestamp DATETIME NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (operation_type),
    INDEX idx_status (status),
    INDEX idx_timestamp (timestamp),
    INDEX idx_severity (severity)
);

-- Traffic data table
CREATE TABLE IF NOT EXISTS traffic_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    congestion_level INT NOT NULL,
    average_speed DECIMAL(5, 2) NOT NULL,
    vehicle_count INT NOT NULL,
    timestamp DATETIME NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_location (location),
    INDEX idx_timestamp (timestamp),
    INDEX idx_congestion (congestion_level)
);

-- Alerts table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    timestamp DATETIME NOT NULL,
    acknowledged BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type (type),
    INDEX idx_severity (severity),
    INDEX idx_acknowledged (acknowledged)
);

-- IoT devices table
CREATE TABLE IF NOT EXISTS iot_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(100) NOT NULL UNIQUE,
    device_type VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_seen DATETIME NOT NULL,
    battery_level INT,
    signal_strength INT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (device_type),
    INDEX idx_status (status),
    INDEX idx_location (location)
);

-- Emergency incidents table
CREATE TABLE IF NOT EXISTS emergency_incidents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    incident_type VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    severity INT NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    reported_at DATETIME NOT NULL,
    resolved_at DATETIME,
    response_team VARCHAR(100),
    eta_minutes INT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (incident_type),
    INDEX idx_status (status),
    INDEX idx_reported (reported_at)
);

-- System metrics table
CREATE TABLE IF NOT EXISTS system_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(50),
    recorded_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (metric_name),
    INDEX idx_recorded (recorded_at)
);

-- AI predictions table
CREATE TABLE IF NOT EXISTS ai_predictions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prediction_type VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    predicted_value DECIMAL(10, 2) NOT NULL,
    confidence DECIMAL(5, 4) NOT NULL,
    prediction_for DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type (prediction_type),
    INDEX idx_location (location),
    INDEX idx_prediction_for (prediction_for)
);

-- Insert sample data
INSERT INTO city_operations (operation_type, status, location, description, severity, timestamp, latitude, longitude) VALUES
('TRAFFIC', 'ACTIVE', 'Highway I-95', 'Major accident blocking two lanes', 5, NOW() - INTERVAL 30 MINUTE, 40.712800, -74.006000),
('SECURITY', 'MONITORING', 'Central Park', 'Increased security patrols', 3, NOW() - INTERVAL 45 MINUTE, 40.782900, -73.965400),
('UTILITY', 'RESOLVED', 'Downtown', 'Scheduled power maintenance completed', 2, NOW() - INTERVAL 2 HOUR, 40.758900, -73.985100),
('EMERGENCY', 'ACTIVE', 'Main Hospital', 'Ambulance dispatch required', 5, NOW() - INTERVAL 15 MINUTE, 40.741100, -73.989700);

INSERT INTO traffic_data (location, congestion_level, average_speed, vehicle_count, timestamp, latitude, longitude) VALUES
('Highway I-95', 9, 15.5, 850, NOW(), 40.712800, -74.006000),
('Main Street', 7, 25.2, 420, NOW(), 40.782900, -73.965400),
('Central Bridge', 4, 45.8, 280, NOW(), 40.758900, -73.985100);

INSERT INTO alerts (type, severity, location, message, timestamp, acknowledged) VALUES
('TRAFFIC', 'HIGH', 'Highway I-95', 'Major congestion detected', NOW(), FALSE),
('SECURITY', 'MEDIUM', 'Central Station', 'Unusual activity detected', NOW() - INTERVAL 10 MINUTE, TRUE),
('UTILITY', 'LOW', 'Power Grid', 'Voltage fluctuation', NOW() - INTERVAL 5 MINUTE, FALSE);

INSERT INTO iot_devices (device_id, device_type, location, status, last_seen, battery_level, signal_strength, latitude, longitude) VALUES
('TRAFFIC_CAM_001', 'CAMERA', 'Highway I-95', 'ONLINE', NOW(), 85, 92, 40.712800, -74.006000),
('TEMP_SENSOR_002', 'SENSOR', 'Central Park', 'ONLINE', NOW(), 78, 88, 40.782900, -73.965400),
('POWER_MON_003', 'MONITOR', 'Financial District', 'ONLINE', NOW(), 92, 95, 40.707400, -74.011300);

INSERT INTO emergency_incidents (incident_type, location, severity, description, status, reported_at, response_team, eta_minutes, latitude, longitude) VALUES
('ACCIDENT', 'Highway I-95 Exit 15', 4, 'Multi-vehicle collision', 'ACTIVE', NOW() - INTERVAL 5 MINUTE, 'EMS Unit 1', 8, 40.712800, -74.006000),
('FIRE', 'Downtown Building', 5, 'Commercial building fire', 'ACTIVE', NOW() - INTERVAL 10 MINUTE, 'Fire Truck 2', 12, 40.758900, -73.985100);
