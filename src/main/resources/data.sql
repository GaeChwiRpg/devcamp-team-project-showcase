-- Day 1~2 시드 (User) + Day 2~3 시드 (Ticket).
INSERT INTO users (name, email, password_hash, role, created_at) VALUES
  ('Admin User',     'admin@example.com',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN',    CURRENT_TIMESTAMP),
  ('Operator Park',  'operator@example.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERATOR', CURRENT_TIMESTAMP),
  ('Member Kim',     'member@example.com',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBER',   CURRENT_TIMESTAMP);

INSERT INTO tickets (title, body, status, priority, requester_id, assignee_id, ai_summary, created_at, updated_at) VALUES
  ('VPN 접속 안 됨',     '재택에서 사내 VPN 연결이 안 됩니다. 어제부터 계속이에요.', 'OPEN',        'HIGH',   3, NULL, '재택 VPN 연결 실패. 어제부터 지속.',          CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('노트북 교체 요청',    '5년 된 노트북이 너무 느립니다.',                          'IN_PROGRESS', 'MEDIUM', 3, 2,    '노트북 노후화로 교체 요청.',                    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('이메일 그룹 추가',    '신규 입사자 hyunjoon@를 dev 그룹에 추가 부탁드립니다.',     'RESOLVED',    'LOW',    3, 2,    '신규 입사자 이메일 그룹 추가 요청.',           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
