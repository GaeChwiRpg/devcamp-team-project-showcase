# INTEGRATION LOG — 일자별 + 담당자별 진행

## 팀 정보

- 가상 4명 팀 (Alex / Bora / Chris / Dasol)
- 기간: 2026-07-27 (월) ~ 2026-07-31 (금) · 5일
- 도메인: 운영 티켓 시스템

## Day 1 (월 2026-07-27) — 기획 + 환경 세팅

### 팀 공통

- 추천 프로젝트 1번(운영 티켓 시스템) 선정 (`DECISIONS.md` D-001)
- 라이프사이클 5 단계 책임자 분담 합의 (D-002)
- 기술 스택 합의 (D-003: Java 21 + Spring Boot 3.3 + JPA + Redis)
- AI 보조 기능 3개 결정 (D-004: 요약 + 우선순위 + 답변 초안)
- 라이프사이클 도구 결정 (D-005: Jira MCP + Playwright MCP + Claude Actions + Sentry MCP)

### 개인

- **Alex**: PRD v1.0 자동 생성 (Jira MCP + Claude Code) → 팀 4명 검토 → Jira 티켓 23개 분해
- **Bora**: 검색 도메인 스키마 사전 검토
- **Chris**: 동시성 baseline (Week 5 학습 자산) 도메인 매핑
- **Dasol**: Sentry 프로젝트 생성 + Sentry MCP 연결 검증

### 진행 메모

- Jira MCP 연결에서 토큰 권한 이슈 → Alex가 30분 디버깅 후 해결
- PRD 페르소나 검토 시 운영자 vs 신청자 구분이 6 공통 필수 기능 매핑과 자연스럽게 연결됨

## Day 2 (화 2026-07-28) — 코딩 시작 (인증/권한 + 베이스라인)

### 개인

- **Alex**: PR #2 — Spring Boot baseline + User 도메인 + Security 골격 (12:00 머지)
- **Chris**: 티켓 도메인 PR #3 작업 시작 (Day 3까지 이어짐)

### 진행 메모

- Alex의 PR #2 머지 후 Chris가 그 위에서 Ticket 도메인 작업 가능 (의존성 chain 정리)
- Day 2 종료: build green, H2 console 접근 가능, User 시드 정상

## Day 3 (수 2026-07-29) — 코딩 본격 + 팀 헌법 + 테스트

### 팀 공통

- 오전 미팅에서 PR #2~3 진행 중 발견한 패턴을 CLAUDE.md 헌법으로 명문화 (PR #4)
- 헌법은 PR #5 이후 모든 작업에 자동 강제

### 개인

- **Chris**: PR #3 — Ticket 도메인 + 락 + 캐시 + 이벤트 통합 (15:00 머지)
- **팀 전원**: PR #4 — CLAUDE.md + commands + hooks (10:30 머지)
- **Bora**: PR #5 — Playwright E2E + e2e.yml CI (17:00 머지)

### 진행 메모

- Bora의 첫 Playwright MCP 활용에서 셀렉터 hallucination 1건 발견 → 즉시 prompt에 "셀렉터 추측 금지" 제약 추가 (다음 시나리오부터 적용)
- `/draft-pr` 커스텀 명령으로 PR 본문 자동 생성 흐름이 작동 (Bora가 PR #5에서 시연)

## Day 4 (목 2026-07-30) — 리뷰 + 운영

### 팀 공통

- AI 리뷰 자동화 도입 후 첫 4 PR(이미 머지된 PR #2~5)에 대한 retroactive 리뷰는 생략하고, PR #7 이후부터 자동 리뷰 활성화
- Day 4 회고: AI 리뷰 first-pass + 팀원 second-pass 운영 정책 합의

### 개인

- **Chris**: PR #6 — Claude GitHub Actions Review 워크플로우 도입 (10:30 머지)
- **Dasol**: PR #7 — Sentry MCP 가이드 + Docker Compose 시연 스택 (15:00 머지)

### AI 리뷰 첫 시연

- PR #7에 대한 AI 리뷰 자동 트리거 → 3개 suggestion 제공:
  1. \`MONITORING.md\`의 Sentry DSN 환경 변수 명시 누락 (Dasol 즉시 수정)
  2. \`docker-compose.yml\`의 mysql 비밀번호 환경 변수화 권장 (Dasol secret으로 옮김)
  3. \`Dockerfile\`의 JAR_FILE 패턴이 multi-jar 빌드 시 모호 (Dasol이 향후 멀티 모듈 시 검토 메모)

## Day 5 (금 2026-07-31) — 통합 + 회고

### 팀 공통

- API-CONTRACT.md 최종 갱신 (5 endpoint 정합)
- LIFECYCLE-COVERAGE.md 5 단계 통과 검증 표 작성
- INTEGRATION-LOG.md (이 문서) 작성

### 개인

- **팀 전원**: PR #8 — 통합 + 마감

### Day 5 회고

#### 가장 효과 컸던 도구

- **Playwright MCP** (Bora): 시나리오 1.5h → 30m
- **Claude GitHub Actions Review** (Chris): 모든 PR에 일관된 룰 적용
- **CLAUDE.md 헌법** (팀 전원): PR #5 이후 의사결정이 흐트러지지 않음

#### 가장 효과 작은 도구

- **Sentry MCP** (Dasol): 실제 트래픽이 없어 시연 단계에서 가치 제한 — Phase 3에서 부하 테스트 + 실제 에러 시나리오로 보강 예정

## 통계

- 총 PR 수: **8** (모두 merged)
- 개인 PR 분담: Alex 2 / Bora 1 / Chris 2 / Dasol 1 / 팀 공동 2
- AI 리뷰가 잡은 진짜 이슈: 3건 (Day 4 PR #7)
- AI hallucination: 1건 (Day 3 Playwright 셀렉터, 즉시 잡음)
- E2E 시나리오: 2개 (성공 + 실패) — Phase 3에서 4개로 확장 예정
- API 계약 변경 횟수: 1회 (Day 2 v0.1 → Day 5 v0.1 통합)

## Phase 3 보강 항목 (Week 10 또는 다음 기수)

- JWT 인증 본격 통합 (현재 X-User-Id 헤더 시연)
- Comment endpoint + AI 답변 초안 기능
- Playwright 시나리오 4개로 확장 (login / triage / status-transition)
- Sentry 실제 트래픽 시연
- AI 보조 기능(요약/우선순위) 정확도 측정

## Week 10 (2026-08-03 ~ 08-07) — 발표·회고·면접 준비

### Week 10 Day 1 (월) — CI 안정화

- **Dasol**: PR #9 — gradle wrapper 추가 + AI Review workflow OIDC/secret 처리 강화 + Spring Boot data.sql 순서 / Redis health 비활성화 / X-User-Id stub 인증 필터 통합
- 배경: PR #1~#8 의 Actions 가 빨갛게 표시되어 발표 시 신뢰도 훼손 우려
- 결과: e2e + ai-review 둘 다 main 에서 green. ANTHROPIC_API_KEY 미등록 시 graceful skip.

### Week 10 Day 2 (화) — Phase 4 산출물

- **팀 전원**: PR #10 — Week 10 finale 4 산출물 통합
  - `presentation/SLIDE-OUTLINE.md` — 12장 슬라이드 골격 + 발표자 분담
  - `presentation/DEMO-SCRIPT.md` — 5분 cURL 데모 스크립트 + Plan B
  - `RETROSPECTIVE.md` — KPT 회고 + Phase 3 우선순위
  - `INTERVIEW-ANSWERS.md` — 5장 답변 흐름 sample 6개
  - `README.md` / `LIFECYCLE-COVERAGE.md` 갱신

### Week 10 후반 — 발표·면접 리허설

- 발표 리허설 3회 (Day 3·4·5). 매 회 시간 측정 + 슬라이드 1~2장 압축.
- 면접 답변 스파링 4명 × 3 라운드. 각자 본인 경험으로 살 붙이기.
- 가장 자주 받은 질문 Top 3 (`SLIDE-OUTLINE.md` Q&A 섹션):
  1. pessimistic vs optimistic 락 결정 근거
  2. AI 리뷰가 잡은 진짜 이슈 vs hallucination 구분 방법
  3. 5일 안에 8 PR 분담 + 통합 갈등 해결 방법
