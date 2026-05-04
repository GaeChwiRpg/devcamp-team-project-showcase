# API Contract — 운영 티켓 시스템 v0.1

> 팀 단위 API 계약. Phase 2 baseline 5 endpoint + 3 미구현(Phase 3 예정).
> 변경은 PR과 함께 이 문서를 갱신. 변경 이력은 끝에 누적.

## Base URL

| 환경 | URL |
| --- | --- |
| 로컬 | `http://localhost:8080` |
| 시연 (Phase 3) | TBD |

## 인증

- Phase 2: `X-User-Id` 헤더 (시연용)
- Phase 3: `Authorization: Bearer <JWT>` (DECISIONS.md D-007 후속)

## Endpoint 목록

| # | Method | URL | 권한 | 상태 |
| --- | --- | --- | --- | --- |
| 1 | POST | `/api/tickets` | 인증 | ✅ Phase 2 |
| 2 | GET | `/api/tickets` | OPERATOR / ADMIN | ✅ Phase 2 |
| 3 | GET | `/api/tickets/{id}` | OPERATOR / ADMIN | ✅ Phase 2 |
| 4 | PUT | `/api/tickets/{id}/status` | OPERATOR / ADMIN | ✅ Phase 2 |
| 5 | PUT | `/api/tickets/{id}/assignee` | ADMIN | ✅ Phase 2 |
| 6 | POST | `/api/tickets/{id}/comments` | 본인 또는 OPERATOR | ⏳ Phase 3 |
| 7 | POST | `/api/tickets/{id}/ai-summary` | OPERATOR / ADMIN | ⏳ Phase 3 |
| 8 | GET | `/api/me/tickets` | 인증 | ⏳ Phase 3 |

---

## 1. POST /api/tickets

### Request

```http
POST /api/tickets
Content-Type: application/json
X-User-Id: 3

{
  "title": "VPN 접속 안 됨",
  "body": "재택에서 VPN 연결이 안 됩니다.",
  "priority": "HIGH"
}
```

### Response 201 Created

```json
{
  "id": 4,
  "title": "VPN 접속 안 됨",
  "body": "재택에서 VPN 연결이 안 됩니다.",
  "status": "OPEN",
  "priority": "HIGH",
  "requesterId": 3,
  "assigneeId": null,
  "aiSummary": null,
  "createdAt": "2026-07-29T10:00:00",
  "updatedAt": "2026-07-29T10:00:00"
}
```

### Errors

- 400 INVALID_REQUEST: title 빈 값, priority 누락 등

---

## 2. GET /api/tickets

### Request

```http
GET /api/tickets?status=OPEN&keyword=VPN&page=0&size=20
```

### Query

- `status` (optional): OPEN / IN_PROGRESS / RESOLVED / CLOSED
- `assigneeId` (optional): Long
- `keyword` (optional): 제목 부분 검색 (대소문자 무시)
- `page` (default 0)
- `size` (default 20)

### Response 200 OK

```json
[
  {"id": 1, "title": "VPN 접속 안 됨", "status": "OPEN", "priority": "HIGH", ...}
]
```

### Errors

- 403 FORBIDDEN: OPERATOR/ADMIN 외 호출

---

## 3. GET /api/tickets/{id}

### Response 200 / 404 NOT_FOUND

---

## 4. PUT /api/tickets/{id}/status

### Request

```json
{ "status": "IN_PROGRESS" }
```

### 상태 전이 규칙

| 현재 | 가능한 다음 |
| --- | --- |
| OPEN | IN_PROGRESS / CLOSED |
| IN_PROGRESS | RESOLVED / OPEN |
| RESOLVED | CLOSED / IN_PROGRESS |
| CLOSED | (불가) |

### Errors

- 409 CONFLICT: 잘못된 전이 (예: OPEN → RESOLVED)

---

## 5. PUT /api/tickets/{id}/assignee

### Request

```json
{ "assigneeId": 2 }
```

### 권한

- ADMIN만 가능

---

## 6~8: Phase 3에서 추가

- POST /api/tickets/{id}/comments
- POST /api/tickets/{id}/ai-summary
- GET /api/me/tickets

상세 스펙은 Phase 3 PR 본문에 명시.

---

## 변경 이력

| 일자 | 버전 | 변경 | 책임자 |
| --- | --- | --- | --- |
| 2026-07-26 | v0.1 | 초안 — endpoint 8 정의, 5개 Phase 2 구현 | 팀원 A |
| (Phase 3) | v0.2 | 6~8 endpoint 추가, JWT 인증 도입 | TBD |
