# LIFECYCLE-COVERAGE — 라이프사이클 5 단계 적용 흔적

> 가상 4명 팀이 운영 티켓 시스템에 5 단계를 어떻게 적용했는지 매트릭스 + 단계별 산출물 추적.
> Phase 2~3가 진행되면서 점진 채워집니다.

## 매트릭스

| 단계 | 책임자 | 책 챕터 (`books/05-week8-...`) | 핵심 도구 | 산출물 (예정 위치) | 상태 |
| --- | --- | --- | --- | --- | --- |
| 1. 기획 | 팀원 A | 9주차 03 | Jira MCP, AI PRD 자동 생성 | `PRD.md` ✅, `docs/jira-tickets.md` (Phase 2) | 골격 ✅ |
| 2. 코딩 | 팀 전원 | 7주차 04~07, 9주차 02·04·05 | `CLAUDE.md`, Commands, Hooks, gh CLI, Sub-agents | `CLAUDE.md` (Phase 2), `API-CONTRACT.md` (Phase 2), `src/` (Phase 2) | 대기 |
| 3. 테스트 | 팀원 B | 9주차 06 | Playwright MCP | `tests/e2e/` (Phase 2), `docs/test-strategy.md` (Phase 2) | 대기 |
| 4. 리뷰 | 팀원 C | 10주차 01 | Claude GitHub Actions, CodeRabbit | `.github/workflows/ai-review.yml` (Phase 2), `docs/review-policy.md` (Phase 3) | 대기 |
| 5. 배포·운영 | 팀원 D | 10주차 06, 02~05 | Sentry MCP, Docker Compose | `MONITORING.md` (Phase 2), `docker-compose.yml` (Phase 2) | 대기 |

## 단계별 적용 흐름

### 1. 기획 (책 9주차 03)

#### 적용 도구

- **Jira MCP**: Claude Code에 Jira MCP 연결 → AI에게 PRD를 Jira 티켓으로 분해 위임
- **AI PRD**: 운영자 페르소나 + 페인포인트 → PRD 초안 자동 생성

#### 적용 흐름 (예정)

1. (Day 1 오전) 팀이 4명 미팅 — 추천 5개 중 1택 합의
2. (Day 1 오후) 팀원 A가 Claude Code에 Jira MCP 연결 후 PRD 초안 자동 생성
3. (Day 1 오후) 팀 4명이 PRD 검토 + 기능 우선순위 합의
4. (Day 1 저녁) AI에게 PRD를 Jira 티켓 23개로 분해 위임
5. (Day 1 저녁) 팀원별 티켓 할당

#### 사람-only 대비 (예상)

- PRD 작성 약 4시간 → 1시간 (AI 초안 + 검토)
- Jira 티켓 23개 분해 약 2시간 → 30분

#### 검증

- PRD 페르소나 정의 1개 + 성공 지표 4개 + 제약 사항 5개 모두 사람 최종 검토

---

### 2. 코딩 (책 7주차 04~07, 9주차 02·04·05)

#### 적용 도구

- **`CLAUDE.md` 팀 헌법** (Phase 2 작성 예정)
  - 도메인 컨텍스트 (티켓 도메인 / 6 공통 필수 기능)
  - 코딩 규칙 (3계층 분리, @Transactional 위치, LAZY 기본)
  - 작업 경계 (서비스 디렉토리 외 수정 금지)
- **Commands** (Phase 2)
  - `.claude/commands/draft-pr.md` — gh CLI로 PR 본문 자동 생성
  - `.claude/commands/check-contract.md` — API 계약 변경 감지
- **Hooks** (Phase 2)
  - `.claude/hooks/pre-tool-use.sh` — 작업 경계 위반 차단
  - `.claude/hooks/post-tool-use.sh` — git diff 시 contract 자동 갱신
- **gh CLI + AI**: PR 본문 자동 작성, 코드 리뷰 코멘트 처리
- **Sub-agents** (선택, Day 3): 락 전략 후보 3개 동시 비교

#### 적용 흐름 (예정)

(Phase 2 진행 시 채워짐)

#### 검증 루프 4단계 (Week 8 정착)

1. prompt 작성 시 페·목·형·제 강제
2. claude.md 검증 규칙 적용
3. PreToolUse hook
4. PR 머지 전 본인 직접 실측

---

### 3. 테스트 (책 9주차 06)

#### 적용 도구

- **Playwright MCP**: Claude Code에 연결 후 PRD User Story → E2E 시나리오 자동 변환
- **시나리오 4~5개** (Phase 2 작성):
  - login-flow.spec.ts (인증)
  - ticket-create.spec.ts (US-001)
  - ticket-triage.spec.ts (US-002 + US-003 통합)
  - ticket-status-transition.spec.ts (US-004)
  - (선택) ticket-search.spec.ts (US-006)

#### 적용 흐름 (예정)

1. PRD 4·5장 (User Stories)을 prompt로 전달
2. AI가 각 Story → Playwright 시나리오 변환
3. 사람이 셀렉터·assertion 수정
4. CI에서 자동 실행 (.github/workflows/e2e.yml)

#### 사람-only 대비 (예상)

- 시나리오 4개 작성 약 6시간 → 2시간

---

### 4. 리뷰 (책 10주차 01)

#### 적용 도구

- **Claude GitHub Actions** (`anthropics/claude-code-action@v1`)
  - PR opened/synchronize 시 자동 트리거
  - 팀 `CLAUDE.md`를 prompt로 전달 → 팀 룰 강제
- (선택) CodeRabbit — Phase 4에서 비교 평가

#### 적용 흐름 (예정)

(Phase 2: workflow 설정. Phase 3: 가짜 PR에서 실제 리뷰 코멘트 시연)

#### AI vs 팀원 리뷰 비교

- AI 강점: 일관성 (모든 PR에 같은 룰 적용), first-pass
- 사람 강점: 비즈니스 의도 검증, 도메인 트레이드오프 판단
- 운영: AI = first-pass, 팀원 = second-pass

---

### 5. 배포·운영 (책 10주차 06)

#### 적용 도구

- **Sentry MCP**: Claude Code에 연결 후 에러 분석 위임
- **Docker Compose**: 시연용 단순 배포 (`docker-compose up`)
- (Phase 4 선택) Terraform AWS — 운영급 시연

#### 적용 흐름 (예정)

1. (Phase 2) Spring Boot에 Sentry SDK 통합 + Sentry 프로젝트 생성
2. (Phase 2) `docker-compose.yml` 작성 (app + redis + mysql)
3. (Phase 3) 시연용 에러 1건을 의도적으로 발생 → AI에게 Sentry 데이터 분석 위임
4. (Phase 3) AI가 root cause 후보 3개 제안 → 팀원 D가 검증 후 fix

#### 한계

- 실제 트래픽이 적어 운영 학습은 시연 수준. Week 10에 부하 테스트 보강.

## 5 단계 통과 검증 (예정)

| 단계 | 산출물 | AI 도구 적용 | 사람 검증 | 통과 |
| --- | --- | --- | --- | --- |
| 기획 | PRD.md ✅, Jira 티켓 (Phase 2) | Jira MCP + AI 분해 | 팀 4명 검토 | 진행 중 |
| 코딩 | CLAUDE.md + API-CONTRACT + 코드 (Phase 2) | claude.md/Commands/Hooks/Sub-agents | 검증 루프 4단계 | 대기 |
| 테스트 | tests/e2e/ 시나리오 (Phase 2) | Playwright MCP | 셀렉터 사람 검수 | 대기 |
| 리뷰 | ai-review.yml + 리뷰 메모 (Phase 3) | Claude Actions | 팀원 second-pass | 대기 |
| 운영 | MONITORING.md + docker-compose (Phase 2~3) | Sentry MCP | 팀원 D root cause 검증 | 대기 |

## Phase 진행 상태

- ✅ **Phase 1 (현재)** — 기획 골격 (README, PRD, DECISIONS, LIFECYCLE-COVERAGE)
- ⏳ **Phase 2** — 코드 baseline + 5 단계 도구 설정
- ⏳ **Phase 3** — 가상 4명 팀 PR 시연 + 통합 로그
- ⏳ **Phase 4** — 마무리 (발표 자료, 회고 sample)
