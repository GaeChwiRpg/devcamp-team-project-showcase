-- Day 1~2 시드. 비밀번호는 BCrypt('password')의 해시 (시연용).
INSERT INTO users (name, email, password_hash, role, created_at) VALUES
  ('Admin User',     'admin@example.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN',    CURRENT_TIMESTAMP),
  ('Operator Park',  'operator@example.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERATOR', CURRENT_TIMESTAMP),
  ('Member Kim',     'member@example.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBER',   CURRENT_TIMESTAMP);
