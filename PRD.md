# PRD — 운영 티켓 시스템

> Product Requirements Document v1.0 · 2026-07-25 작성 · 가상 4명 팀

## 1. Executive Summary

작은 운영팀이 사내 문의 / 작업 / 인시던트를 기록하고 담당자에게 배정해서 해결까지 추적하는 시스템. AI가 들어오는 티켓을 요약하고 우선순위를 추천해서 운영자의 트리아지(triage) 시간을 절감한다.

## 2. Problem Statement

### 사용자가 마주하는 문제

- 운영팀이 받는 문의가 채팅·이메일·슬랙으로 흩어져 있음 → 누락·중복 발생
- 우선순위 판단이 매번 사람 머리에 의존 → 일관성 떨어짐
- 인계할 때 "이 티켓 무슨 내용이지?"를 매번 다시 읽어야 함
- 같은 유형 티켓이 반복되는데 패턴 학습이 없음

### 본 시스템이 해결하는 것

- 티켓을 한 곳에 모아 상태·담당자·우선순위로 추적
- AI가 티켓 본문을 1줄 요약하고 우선순위 후보 제시 → 운영자는 검토·수정만
- 인계 시 AI 요약을 그대로 활용
- 비슷한 티켓 검색으로 패턴 학습 가능

## 3. Goals & Success Metrics

### Goals (우선순위 순)

1. 티켓이 흩어지지 않게 한 곳에 모은다
2. 우선순위 트리아지 시간을 줄인다
3. 인계 비용을 줄인다

### Success Metrics

| 지표 | 측정 방법 | 목표 |
| --- | --- | --- |
| 일일 처리 티켓 수 | DB count | 시연 환경 30+/day |
| 평균 트리아지 시간 | 티켓 생성 → 우선순위 확정까지 | 5분 이하 (AI 요약 후 검토 1분 + 운영자 확정 4분) |
| AI 우선순위 추천 정확도 | 운영자 채택률 | 60% 이상 (운영자가 추천 그대로 사용한 비율) |
| 인계 시간 단축 | 인계 회의 시간 | 사람-only 대비 50% 단축 (가설) |

## 4. User Personas

### Persona 1 — 운영자 (대상 사용자)

- 이름: 박미진 (가상)
- 역할: 사내 IT 운영팀 시니어
- 일과: 평일 09:00~18:00 티켓 처리
- 페인포인트: 우선순위 판단의 인지 부하, 인계 시 컨텍스트 손실
- 본 시스템에서 기대하는 것: 들어온 티켓에 빠르게 반응 + AI가 1차 분류해 줘서 인지 부하 감소

### Persona 2 — 신청자 (간접 사용자)

- 이름: 김상용 (가상)
- 역할: 사내 다른 팀의 일반 직원
- 페인포인트: 본인 신청이 어디까지 처리됐는지 모름
- 본 시스템에서 기대하는 것: 진행 상태가 보이고 담당자가 누구인지 알 수 있음

## 5. User Stories & Use Cases

### US-001: 티켓 생성

신청자로서, 제목·본문·우선순위 후보를 입력해서 티켓을 만들 수 있다.

### US-002: 티켓 자동 요약

티켓이 생성되면 AI가 본문을 1줄 요약 + 우선순위 후보를 제시한다.

### US-003: 운영자 트리아지

운영자로서, 미해결 티켓 목록을 우선순위 + 생성 시각 순으로 보고 본인에게 배정할 수 있다.

### US-004: 상태 전이

운영자로서, 티켓 상태를 OPEN → IN_PROGRESS → RESOLVED → CLOSED로 전이시킬 수 있다.

### US-005: 댓글

운영자/신청자로서 티켓에 댓글을 달 수 있고, AI가 답변 초안을 제시할 수 있다.

### US-006: 검색·필터

운영자로서, 상태·담당자·우선순위·키워드로 티켓을 필터링할 수 있다.

### US-007: 변경 알림

티켓 상태가 바뀌면 신청자에게 비동기 알림이 발송된다 (이벤트 기반).

## 6. Functional Requirements

### 도메인

- `Ticket`: id, title, body, status, priority, requester_id, assignee_id, created_at, updated_at
- `User`: id, name, role (admin/operator/member), email
- `Comment`: id, ticket_id, author_id, body, ai_drafted (bool), created_at
- `TicketEvent`: id, ticket_id, event_type, payload, occurred_at (이벤트 소싱 보조용)

### API endpoint (8개)

| Method | URL | 의도 | 권한 |
| --- | --- | --- | --- |
| POST | /api/tickets | 티켓 생성 | member+ |
| GET | /api/tickets | 목록 (필터·검색) | operator+ |
| GET | /api/tickets/{id} | 단건 + 댓글 | 본인 또는 operator+ |
| PUT | /api/tickets/{id}/status | 상태 전이 | operator+ |
| PUT | /api/tickets/{id}/assignee | 담당자 배정 | admin |
| POST | /api/tickets/{id}/comments | 댓글 추가 | 본인 또는 operator+ |
| POST | /api/tickets/{id}/ai-summary | AI 요약 트리거 | operator+ |
| GET | /api/me/tickets | 본인이 신청한 티켓 | member+ |

### 6 공통 필수 기능 매핑

| 공통 기능 | 본 시스템 매핑 |
| --- | --- |
| 권한·역할 | admin / operator / member 3 단계 + 본인 티켓만 보기 |
| 핵심 트랜잭션 | 상태 전이 + 담당자 배정 + 이벤트 발행 묶음 |
| 검색·필터 | 상태·담당자·우선순위·키워드 복합 필터 |
| 캐시 | 미해결 티켓 카운터 (`open-ticket-count`) + 본인 트리아지 큐 |
| 비동기·이벤트 | TicketEvent 발행 + 신청자 알림 (Spring Events 또는 Outbox 패턴) |
| AI 보조 | 티켓 요약 + 우선순위 추천 + 댓글 답변 초안 |

## 7. Non-Functional Requirements

- **성능**: 티켓 목록 p95 < 200ms (시드 1만 row 환경)
- **가용성**: 시연용 단일 인스턴스, 다음 기수에 멀티 인스턴스 검토
- **보안**: JWT 인증 + role 기반 인가, AI 요약에 PII 마스킹 옵션
- **관측성**: Sentry 에러 모니터링, Spring Actuator metrics 노출

## 8. Out of Scope (이번 미션 범위 밖)

- 모바일 앱
- 실시간 채팅 (티켓 댓글로 대체)
- 다국어 (한국어만)
- 외부 시스템 연동 (Slack/이메일 실제 발송 — 시연용 로그만)
- 결제·정산
- AI 모델 학습 (Claude API 사용만)

## 9. Technical Architecture

```
[Browser/Postman] → [Spring Boot API] → [JPA] → [MySQL/H2]
                          ↓
                    [Redis Cache] (미해결 카운터)
                          ↓
                    [Spring Events] → [알림 핸들러]
                          ↓
                    [Claude API] (AI 요약/추천)
                          ↓
                    [Sentry] (에러 모니터링)
```

## 10. Release Plan

### Week 9 (구현)

- Day 1 (월): 기획 + 환경 세팅 + 팀 분담
- Day 2 (화): 도메인 + 인증 + 기본 CRUD
- Day 3 (수): API 계약 정합 + 검색·필터 + 캐시
- Day 4 (목): 동시성·트랜잭션 + AI 보조 기능 + E2E 테스트
- Day 5 (금): 통합 + 운영 도구 + 회고

### Week 10 (통합·발표)

- 배포 가이드 + 부하 테스트
- README + 발표 자료
- 면접 답변 시나리오

## 11. Success Criteria (MVP 완성 기준)

- [ ] 8개 endpoint 모두 동작
- [ ] 6 공통 필수 기능 모두 포함
- [ ] 라이프사이클 5 단계 적용 흔적 (LIFECYCLE-COVERAGE.md 기준)
- [ ] AI 보조 기능 정확도 측정 결과 (운영자 채택률 60% 이상)
- [ ] E2E 시나리오 4개 통과
- [ ] AI 리뷰 자동 트리거 + 사람 second-pass 흐름 동작
- [ ] Sentry 모니터링 + 1회 이상 시연용 에러 분석

## 12. Approval

- 작성: 가상 4명 팀 (Day 1)
- 검토: 팀 전원 (Day 1 종료 시)
- 변경 이력: `DECISIONS.md`에 기록
