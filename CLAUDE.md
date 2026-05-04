# CLAUDE.md — 팀 헌법

> 가상 4명 팀이 운영 티켓 시스템을 만들면서 AI(Claude Code)에게 강제하는 규칙 모음.
> 모든 prompt에 자동으로 포함되며 어겨지면 hook이 차단한다.

## 도메인 컨텍스트

### 시스템

- **운영 티켓 / 작업 관리 시스템** — 사내 IT 운영팀 + 신청자
- 페르소나: 운영자(operator), 신청자(member), 관리자(admin)
- 핵심 흐름: 티켓 생성 → AI 자동 요약 → 운영자 트리아지 → 상태 전이 → 알림 → 종료

### 도메인 모델

- `Ticket(id, title, body, status, priority, requester_id, assignee_id, ai_summary, created_at, updated_at)`
- `User(id, name, email, password_hash, role, created_at)` — role: ADMIN / OPERATOR / MEMBER
- `Comment(id, ticket_id, author_id, body, ai_drafted, created_at)`
- 상태 전이 규칙: OPEN → IN_PROGRESS → RESOLVED → CLOSED (역순 일부 가능, `TicketStatus.canTransitionTo` 참고)

### 핵심 쿼리

- `GET /api/tickets`: status / assignee / keyword 복합 필터 + priority desc + created_at desc 정렬
- 인덱스: `(status, created_at DESC)`, `(assignee_id, status)`

## 6 공통 필수 기능 매핑

| 기능 | 본 시스템 매핑 |
| --- | --- |
| 권한·역할 | ADMIN / OPERATOR / MEMBER 3 단계 + Spring Security `@PreAuthorize` |
| 핵심 트랜잭션 | 상태 전이 + 담당자 배정 (락 + 이벤트 발행 묶음) |
| 검색·필터 | TicketRepository.search (status/assignee/keyword/priority) |
| 캐시 | `open-ticket-count` (`@Cacheable`) + `@CacheEvict` on create/changeStatus |
| 비동기·이벤트 | `TicketStatusChangedEvent` Spring Events |
| AI 보조 | AiSummaryService.summarize (Phase 3에서 Claude API) |

## 코딩 규칙

### 3계층 분리 (Week 1 학습)

- Controller (`api/`) — HTTP 입출력만
- Service (`service/`) — 비즈니스 흐름 + 트랜잭션 경계
- Domain + Repository (`domain/`) — 도메인 객체 + 저장만

### @Transactional 위치 (Week 1 학습)

- Controller에 절대 안 붙임
- 단일 read는 안 붙임 (cost > benefit)
- 묶음 read+write에만 붙임 (`create`, `changeStatus`, `assignTo`)

### LAZY 기본 (Week 2 학습)

- `@ManyToOne` / `@OneToMany` 모두 LAZY
- N+1 발견 시 `@EntityGraph` 또는 fetch join 적용 + evidence 남김

### 락 전략 (Week 5 학습)

- 동시 변경 가능한 메서드는 `@Lock(PESSIMISTIC_WRITE)` (`findByIdForUpdate`)
- DECISIONS.md D-006 후속 평가 결과 따라 변경 가능

### 캐시 전략 (Week 7 학습)

- 변경 빈도 << 조회 빈도 인 endpoint만 캐시
- `@CacheEvict(allEntries=true)`로 일괄 무효화 (TTL은 Phase 3 추가 검토)

## 작업 경계

- 미션 외 디렉토리 수정 금지 (`.claude/hooks/pre-tool-use.sh`가 차단)
- `src/main/` 외 폴더는 readonly로 본다 (예: `docs/`, `tests/e2e/`는 별도 PR)
- 비밀 정보 (`.env`, JWT secret, API key) commit 절대 금지

## 출력 형식

- 답을 통째로 주지 말고 단계별 reasoning 먼저
- API 변경은 반드시 `API-CONTRACT.md` 업데이트 동반
- 측정 결과는 본인 실측만 사용 (AI 추정값 금지)

## AI 검증 규칙

- AI가 만든 응답시간/throughput 추정 수치는 evidence 사용 금지 — 본인 hey/wrk 측정만
- AI hallucination 사례는 즉시 `evidence/failure-cases.md`에 추가
- 모든 prompt는 페·목·형·제 4 요소 강제

## 라이프사이클 단계 책임자

각 단계는 책임자 1명이 산출물 끝까지 책임. 단계 간 협업은 PR 본문 + INTEGRATION-LOG.md로 동기화.

## 변경 절차

이 헌법을 바꾸는 결정은 모두 `DECISIONS.md`에 새 항목으로 추가. 헌법 그대로 덮어쓰기 금지.
