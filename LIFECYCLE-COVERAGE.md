# LIFECYCLE-COVERAGE — 라이프사이클 5 단계 적용 흔적

> 가상 4명 팀이 운영 티켓 시스템에 5 단계를 어떻게 적용했는지 매트릭스 + 단계별 산출물 추적.

## 매트릭스 (Phase 2 종료 시점)

| 단계 | 책임자 | 핵심 도구 | 산출물 | 상태 |
| --- | --- | --- | --- | --- |
| 1. 기획 | 팀원 A | Jira MCP, AI PRD | [`PRD.md`](./PRD.md), [`DECISIONS.md`](./DECISIONS.md) | Phase 1 ✅ · Jira 티켓 시연은 Phase 3 |
| 2. 코딩 | 팀 전원 | claude.md, Commands, Hooks | [`CLAUDE.md`](./CLAUDE.md), [`API-CONTRACT.md`](./API-CONTRACT.md), `src/`, `.claude/` | Phase 2 ✅ |
| 3. 테스트 | 팀원 B | Playwright MCP | [`tests/e2e/`](./tests/e2e/), [`.github/workflows/e2e.yml`](./.github/workflows/e2e.yml) | Phase 2 ✅ (sample 1개) · 시나리오 4개 확장은 Phase 3 |
| 4. 리뷰 | 팀원 C | Claude GitHub Actions | [`.github/workflows/ai-review.yml`](./.github/workflows/ai-review.yml) | Phase 2 ✅ (workflow) · 실제 리뷰 시연은 Phase 3 |
| 5. 배포·운영 | 팀원 D | Sentry MCP, Docker | [`MONITORING.md`](./MONITORING.md), [`docker-compose.yml`](./docker-compose.yml), `Dockerfile` | Phase 2 ✅ (설정) · 시연용 에러 분석은 Phase 3 |

## 단계별 적용 흐름

### 1. 기획

- **PRD.md**: 페르소나 2명 + User Stories 7개 + endpoint 8개 + 6 공통 필수 기능 매핑
- **DECISIONS.md**: 의사결정 6건 (주제 / 분담 / 스택 / AI 기능 / 도구 / 락 전략)
- (Phase 3) Jira MCP 연결 + AI 티켓 분해 시연 → `docs/jira-tickets.md` 추가 예정

### 2. 코딩

- **CLAUDE.md** 팀 헌법: 도메인 컨텍스트 + 6 공통 필수 기능 매핑 + 코딩 규칙 (3계층/Transactional/LAZY/락/캐시) + 작업 경계 + AI 검증 규칙
- **API-CONTRACT.md**: Phase 2 5 endpoint 정의 + 변경 이력
- **`src/`**: Spring Boot 3.3 + Java 21 + JPA + Spring Security + Redis 통합 baseline
  - 도메인: User / Ticket / Comment + 상태 전이 규칙 + 인덱스
  - 서비스: TicketService (락 + 캐시 + 이벤트 + AI), AiSummaryService (stub)
  - API: TicketController (5 endpoint, @PreAuthorize 권한 정책)
  - Security: SecurityConfig (Phase 2 permitAll, Phase 3 JWT 통합 예정)
- **`.claude/commands/`**: `draft-pr.md` (PR 본문 자동 작성), `check-contract.md` (API 변경 감지)
- **`.claude/hooks/pre-tool-use.sh`**: 비밀 정보·.git·레포 외부 수정 차단
- **`.claude/settings.json`**: PreToolUse hook 등록

### 3. 테스트

- **tests/e2e/ticket-create.spec.ts**: Playwright 시나리오 sample (성공 + 실패 케이스)
- **tests/e2e/playwright.config.ts**: BASE_URL 설정 + Phase 2용 X-User-Id 헤더 자동 주입
- **.github/workflows/e2e.yml**: bootJar 빌드 → 백그라운드 실행 → Playwright 자동 실행 → report 아티팩트
- (Phase 3) login-flow / ticket-triage / ticket-status-transition 시나리오 추가

### 4. 리뷰

- **.github/workflows/ai-review.yml**: Claude GitHub Actions Review
  - PR opened/synchronize 자동 트리거
  - 팀 CLAUDE.md 핵심 규칙을 prompt로 전달 (3계층/Transactional/LAZY/락/캐시/API-CONTRACT 강제)
  - 한국어 리뷰, 비전공자 톤, max 5 suggestions
- (Phase 3) 가짜 4명 팀 PR 4~8개에서 실제 리뷰 시연 + AI vs 팀원 비교 메모

### 5. 배포·운영

- **MONITORING.md**: Sentry SDK 통합 가이드 + Sentry MCP 연결 명령 + 운영 시나리오 + 평가 기준
- **docker-compose.yml**: app + mysql 8 + redis 7 시연용 스택 (healthcheck 포함)
- **Dockerfile**: Temurin 21 JRE alpine 기반
- (Phase 3) 시연용 에러 1건 의도적 발생 → Sentry MCP에 위임 → root cause 후보 검증

## Phase 진행 상태

- ✅ **Phase 1** — 기획 골격 (README, PRD, DECISIONS, LIFECYCLE-COVERAGE 초안)
- ✅ **Phase 2** — 코드 baseline + 5 단계 도구 설정
- ✅ **Phase 3** — 가상 4명 팀 PR 시연 (8 PR + INTEGRATION-LOG Day 1~5)
- ✅ **Phase 4 (현재)** — Week 10 finale (SLIDE-OUTLINE / DEMO-SCRIPT / RETROSPECTIVE / INTERVIEW-ANSWERS)

## Phase 2 통과 검증

| 단계 | 산출물 존재 | AI 도구 설정 | 통과 |
| --- | --- | --- | --- |
| 기획 | PRD.md, DECISIONS.md | (Phase 3 Jira MCP) | ✅ 기획 골격 |
| 코딩 | src/, CLAUDE.md, API-CONTRACT.md, .claude/ | claude.md/Commands/Hooks 모두 | ✅ |
| 테스트 | tests/e2e/, e2e.yml workflow | Playwright MCP 시나리오 sample | ✅ |
| 리뷰 | ai-review.yml | Claude Actions 통합 | ✅ |
| 운영 | MONITORING.md, docker-compose.yml, Dockerfile | Sentry MCP 가이드 | ✅ |
