# Dev Camp Team Project Showcase — 운영 티켓 시스템

> 딩코딩코 백엔드 부트캠프 팀 프로젝트 최종 시연 (PR 시리즈로 진행)

가상 4명 팀이 Week 9 (5일)에 운영 티켓 시스템을 만들어가는 과정을 PR 시리즈 8개로 추적합니다.

## 가상 팀

- 팀원 A — **Alex Park** (인증/권한 + 기획 책임)
- 팀원 B — **Bora Kim** (핵심 조회 + 테스트 책임)
- 팀원 C — **Chris Lee** (동시성/상태 전이 + 리뷰 책임)
- 팀원 D — **Dasol Yoon** (캐시·AI + 운영 책임)

## 학생이 봐야 하는 것

1. PR 탭에서 [#1 → #8 시리즈](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pulls?q=is%3Apr) 따라 읽기
2. PR 본문의 라이프사이클 단계 + 담당자 + 의사결정 추적
3. AI 리뷰 코멘트 + 사람 응답 시연 (PR #6 이후)
4. [`INTEGRATION-LOG.md`](./INTEGRATION-LOG.md)에 일자별 + 담당자별 진행

이 레포는 학생 팀이 부트캠프 끝났을 때 만들 수 있는 결과물의 본보기입니다.

## 진행 상태 (Day 5 종료)

| Day | 단계 | 담당자 | PR | 상태 |
| --- | --- | --- | --- | --- |
| Day 1 | 기획 | Alex | [#1](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/1) | ✅ merged |
| Day 1~2 | 코딩 (인증/권한) | Alex | [#2](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/2) | ✅ merged |
| Day 2~3 | 코딩 (티켓 도메인) | Chris | [#3](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/3) | ✅ merged |
| Day 3 | 코딩 (팀 헌법) | 팀 전원 | [#4](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/4) | ✅ merged |
| Day 3 | 테스트 | Bora | [#5](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/5) | ✅ merged |
| Day 4 | 리뷰 | Chris | [#6](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/6) | ✅ merged |
| Day 4 | 운영 | Dasol | [#7](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/7) | ✅ merged |
| Day 5 | 통합 | 팀 전원 | [#8](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/8) | ✅ merged |
| Week 10 | CI 보강 | Dasol | [#9](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/9) | ✅ merged |
| Week 10 | 발표·회고·면접 | 팀 전원 | [#10](https://github.com/GaeChwiRpg/devcamp-team-project-showcase/pull/10) | ✅ merged |

## 핵심 문서

### 산출물 (Phase 1~3)

- [`PRD.md`](./PRD.md) — 제품 요구사항
- [`DECISIONS.md`](./DECISIONS.md) — 의사결정 로그 6건
- [`CLAUDE.md`](./CLAUDE.md) — 팀 헌법
- [`API-CONTRACT.md`](./API-CONTRACT.md) — API 계약 + 변경 이력
- [`LIFECYCLE-COVERAGE.md`](./LIFECYCLE-COVERAGE.md) — 5 단계 적용 흔적 매트릭스
- [`INTEGRATION-LOG.md`](./INTEGRATION-LOG.md) — Day 1~5 일자별 진행
- [`MONITORING.md`](./MONITORING.md) — 운영 모니터링 가이드

### Week 10 마무리 (Phase 4)

- [`presentation/SLIDE-OUTLINE.md`](./presentation/SLIDE-OUTLINE.md) — 12장 슬라이드 골격 + 발표자 분담
- [`presentation/DEMO-SCRIPT.md`](./presentation/DEMO-SCRIPT.md) — 5분 라이브 데모 cURL 시나리오 + Plan B
- [`RETROSPECTIVE.md`](./RETROSPECTIVE.md) — KPT 회고 + Phase 3 우선순위
- [`INTERVIEW-ANSWERS.md`](./INTERVIEW-ANSWERS.md) — 5장 답변 흐름으로 6개 면접 질문 sample

## 5 단계 통과 검증

| 단계 | 산출물 | AI 도구 | 통과 |
| --- | --- | --- | --- |
| 기획 | PRD + DECISIONS | Jira MCP + AI PRD | ✅ PR #1 |
| 코딩 | src/ + CLAUDE + API-CONTRACT | claude.md + Commands + Hooks | ✅ PR #2~4 |
| 테스트 | tests/e2e/ + e2e.yml | Playwright MCP | ✅ PR #5 |
| 리뷰 | ai-review.yml | Claude GitHub Actions | ✅ PR #6 |
| 운영 | MONITORING + docker-compose | Sentry MCP 가이드 | ✅ PR #7 |

## 학습 보조

- 부트캠프 학생 sample: [`GaeChwiRpg/devcamp-submission-sample`](https://github.com/GaeChwiRpg/devcamp-submission-sample) (개인 미션)
- 팀 PR 텍스트 sample: [`GaeChwiRpg/devcamp-team-submission-sample`](https://github.com/GaeChwiRpg/devcamp-team-submission-sample)
