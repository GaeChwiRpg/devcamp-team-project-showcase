# Dev Camp Team Project Showcase — 운영 티켓 시스템

> 딩코딩코 백엔드 부트캠프 **팀 프로젝트 최종 시연** · 4명 팀이 Week 9~10에 만들어낼 결과물의 본보기

이 저장소는 부트캠프 8주 학습이 끝난 학생 팀이 **2주(Week 9~10)에 만들 수 있는 결과물의 수준과 흐름**을 보여주는 시연 레포입니다. 학생이 미션 시작 시 "우리 팀은 어디까지 갈 수 있는가"를 한눈에 볼 수 있게 하는 동기부여 자산입니다.

## 학생이 봐야 하는 것

이 레포는 단순한 코드 모음이 아닙니다. **부트캠프 학습 라이프사이클 5 단계가 어떻게 팀 협업으로 적용되는지**를 단계별로 추적할 수 있게 만들어졌습니다.

처음 본다면 5분 안에 다음 순서로 보세요:

1. 이 README — 프로젝트 정체와 학습 의도
2. [`PRD.md`](./PRD.md) — 제품 요구사항 (왜 이 시스템을 만들었나)
3. [`DECISIONS.md`](./DECISIONS.md) — 주제 선정·도구 선택·역할 분담 의사결정 로그
4. [`LIFECYCLE-COVERAGE.md`](./LIFECYCLE-COVERAGE.md) — 5 단계 누가 무엇을 적용했는지 매트릭스
5. (Phase 2 이후) 코드와 PR diff

## 프로젝트 개요

| 항목 | 값 |
| --- | --- |
| **도메인** | 운영 티켓 / 작업 관리 시스템 (추천 프로젝트 1번) |
| **기간** | Week 9~10 (가상 4명 팀, 2주) |
| **기술 스택** | Spring Boot 3.3 / Java 21 / JPA / H2·MySQL / Redis / Docker |
| **AI 도구** | Claude Code, Jira MCP, Playwright MCP, Claude GitHub Actions, Sentry MCP |
| **공통 필수 기능 6개** | 권한·트랜잭션·검색·캐시·비동기·AI 보조 모두 포함 |

## 부트캠프와의 연결

| Week | 학생이 익히는 것 | 이 레포에서 보는 것 |
| --- | --- | --- |
| Week 1~7 | Spring Boot / JPA / 인덱스 / 동시성 / 프로파일링 / Redis | 코드 base에 그대로 녹아 있음 |
| Week 8 | AI Native 6 요소 + 라이프사이클 2 단계 (개인) | 팀 헌법 + Commands + Hooks + 페목형제 prompt가 이 레포에 적용 |
| **Week 9** | 라이프사이클 5 단계 팀 분담 | **이 레포 자체가 그 결과물** |
| Week 10 | 통합 + 발표 + 면접 전환 | README/발표 자료 sample 포함 (Phase 4) |

## 주제를 운영 티켓 시스템으로 고른 이유

[`DECISIONS.md`](./DECISIONS.md)에 자세히. 요약:

- 6 필수 기능이 도메인적으로 자연스럽게 매핑 (권한 = admin/member, 트랜잭션 = 상태 전이, 검색 = 우선순위 필터, 캐시 = 미해결 카운터, 비동기 = 변경 알림, AI = 티켓 요약/우선순위 추천)
- 비전공자도 1분 안에 도메인 이해
- 학생이 다음 기수에 다른 추천 주제로 비교 학습하기 쉬움

## 라이프사이클 5 단계 적용 (개요)

| 단계 | 책임자 (가상 4명 팀) | 책 챕터 | 권장 도구 | 산출물 |
| --- | --- | --- | --- | --- |
| 1. 기획 | 팀원 A | 9주차 03 | Jira MCP, AI PRD | `PRD.md`, Jira 티켓 분해 |
| 2. 코딩 | 팀 전원 | 7주차 04~07, 9주차 02·04·05 | claude.md, Commands, Hooks, Sub-agents | `CLAUDE.md`, `API-CONTRACT.md`, `src/` |
| 3. 테스트 | 팀원 B | 9주차 06 | Playwright MCP | `tests/e2e/` |
| 4. 리뷰 | 팀원 C | 10주차 01 | Claude GitHub Actions | `.github/workflows/ai-review.yml` |
| 5. 배포·운영 | 팀원 D | 10주차 06 | Sentry MCP, Docker | `MONITORING.md`, `docker-compose.yml` |

상세는 [`LIFECYCLE-COVERAGE.md`](./LIFECYCLE-COVERAGE.md).

## 개발 진행 단계 (이 레포가 채워지는 순서)

이 sample 레포는 다음 4 단계로 점진 완성됩니다:

- **Phase 1 (현재)** — 기획 골격: README, PRD, DECISIONS, LIFECYCLE-COVERAGE
- **Phase 2** — 코드 baseline + 5 단계 도구 설정
- **Phase 3** — 가상 4명 팀 PR 시연 (feature 브랜치별 PR + 통합 로그)
- **Phase 4** — 마무리 (발표 자료, 회고 sample, 마케팅 README)

각 Phase commit이 git 히스토리에 보이게 작성합니다.

## 학생 팀에게 주는 메시지

이 레포의 모든 산출물은 **"한 가지 풀이 예시"**입니다. 정답이 아닙니다. 본인 팀이 다른 주제·다른 도구·다른 분담으로 가도 됩니다. 단:

- 라이프사이클 **5 단계 모두 적용 흔적**이 보여야 함
- 공통 필수 기능 **6개 모두** 포함
- 개인 PR **2개 이상** + PR마다 라이프사이클 단계 명시
- AI를 써서 구현 속도는 올리되 **품질 통제는 사람이 유지**

## 관련 레포

- 학생 개인 제출 sample: [`GaeChwiRpg/devcamp-submission-sample`](https://github.com/GaeChwiRpg/devcamp-submission-sample)
- 팀 PR 텍스트 sample: [`GaeChwiRpg/devcamp-team-submission-sample`](https://github.com/GaeChwiRpg/devcamp-team-submission-sample) (PR 본문 형식 + 역할 분담 매트릭스)
- 운영진 정답 reference (private): `GaeChwiRpg/devcamp-answer-keys`

## 라이선스

이 sample 레포는 부트캠프 학습용 시연 자산입니다. 학생 팀이 본인 프로젝트의 시작점으로 활용 가능합니다.
