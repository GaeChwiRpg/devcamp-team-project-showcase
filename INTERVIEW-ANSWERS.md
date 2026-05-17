# 면접 답변 sample — 운영 티켓 시스템

> Week 10 책 9장의 5장 답변 흐름(Problem · Options · Decision · Action · Result)을 이 프로젝트 경험으로 채운 sample.
> 면접관이 자주 묻는 6개 질문에 한해 작성. 본인 경험으로 살을 붙여 사용.

## 5장 답변 흐름 요약

| 장 | 핵심 질문 | 한 줄 |
| --- | --- | --- |
| 1. Problem | 무엇이 문제였나? | 비즈니스 현상 + 기술 증상 |
| 2. Options | 어떤 선택지를 봤나? | 최소 2개 + 각각의 trade-off |
| 3. Decision | 왜 그걸 골랐나? | 측정/제약/팀 합의 근거 |
| 4. Action | 어떻게 구현했나? | 핵심 코드 1~2 줄 + 검증 방법 |
| 5. Result | 무엇이 바뀌었나? | 정량 수치 + 한계 + 다음 액션 |

## Q1. "본인이 가장 깊게 다룬 기술 결정 한 가지를 5장으로 풀어보세요."

> **추천 주제**: 동시 상태 전이 시 락 전략 결정 (`DECISIONS.md` D-006).

**Problem (1장)** — 운영자 2명이 동시에 같은 티켓의 상태를 변경할 때 마지막 변경만 반영되는 데이터 정합성 문제가 있었습니다. PR #3 코드 리뷰에서 Chris 팀원이 `changeStatus` 메서드의 read-then-write 구간에 락이 없음을 지적했습니다.

**Options (2장)** — 두 가지 옵션을 검토했습니다.
첫째, optimistic lock (`@Version`): 충돌 시 재시도 로직 + 사용자에게 충돌 알림. 충돌이 적으면 최선이지만 충돌 빈도 모르면 재시도 코드가 부담.
둘째, pessimistic lock (`@Lock(PESSIMISTIC_WRITE)`): 락이 충돌 자체를 방지. 동시성이 떨어지지만 운영자 수 ~100명 가정에서는 문제 없음.

**Decision (3장)** — pessimistic 을 골랐습니다. 근거 3개:
(1) 사내 운영팀 사용자 수 100명 이하 가정. 동시 변경 충돌 빈도 자체가 낮음.
(2) 신입 팀이라 재시도 로직 + 충돌 UI 까지 5일 안에 완성하는 부담이 큼.
(3) `DECISIONS.md` D-006 에 명시된 "Phase 3 에서 측정 후 재평가" 라는 후속 절차가 있음. 결정을 박지 않고 검증 가능한 형태로 남김.

**Action (4장)** — `TicketRepository.findByIdForUpdate(id)` 에 `@Lock(PESSIMISTIC_WRITE)` 어노테이션 추가, `TicketService.changeStatus` 의 read 단계를 이 메서드로 교체. 단위 테스트는 두 트랜잭션이 동시에 같은 티켓을 변경할 때 한쪽이 대기하는지 검증 (`@Transactional` + `CountDownLatch`).

**Result (5장)** — 통합 데모에서 운영자 시뮬레이션 (`xargs -P 5`) 시 마지막 변경만 반영되던 현상이 모두 적용됨으로 바뀌었습니다. 한계: 부하 테스트 정량 수치는 Phase 3 로 미뤘습니다. hey 동시 100 요청 시나리오로 응답시간/실패율 비교는 `evidence/lock-comparison.md` 에 추가 예정입니다.

## Q2. "AI 코딩 도구를 어떻게 검증했습니까? hallucination 사례를 들어주세요."

**Problem** — Day 3 첫 Playwright 시나리오 작성 시, Claude 가 `data-testid="ticket-create-button"` 셀렉터를 자동 생성했지만 실제 UI 에는 그런 testid 가 없었습니다. 시나리오 실행 시 selector not found.

**Options** — (1) 매번 사람이 셀렉터 검증, (2) prompt 에 "셀렉터 추측 금지" 제약 추가, (3) Playwright codegen 으로만 셀렉터 생성. 1번은 자동화 가치 사라짐, 3번은 학습 시간 추가, 2번이 가장 빠른 보강.

**Decision** — 2번. 즉시 prompt 제약 추가 + `evidence/failure-cases.md` 에 hallucination 1건 기록. 다음 시나리오부터 hallucination 0건.

**Action** — `.claude/commands/draft-pr.md` 에 "셀렉터는 codegen 결과 또는 실제 페이지 inspect 결과만 사용. 추측 금지." 한 줄 제약 추가.

**Result** — 1건 잡고 → prompt 박고 → 다음 시나리오부터 0건. 효과: hallucination 잡는 사이클을 5일 안에 1번 돌려보니 다른 도구(Claude Actions Review)도 같은 패턴으로 다룰 수 있게 됨.

## Q3. "팀에서 의견 충돌이 있었나요? 어떻게 해결했습니까?"

**Problem** — Day 3 에 캐시 전략을 두고 의견 갈림. Dasol 은 모든 GET 에 캐시, Bora 는 `/api/tickets/open-count` 만 캐시. CLAUDE.md 헌법 작성 중이라 결정이 시급.

**Options** — (1) 모든 GET 에 캐시 + 변경 메서드마다 evict, (2) 변경 빈도 << 조회 빈도인 endpoint 만, (3) 결정 보류하고 Phase 3 측정 후 결정.

**Decision** — 2번. 근거: (a) Phase 2 단계라 캐시 hit rate 측정 불가, (b) 모든 GET 캐싱은 evict 로직이 복잡해져 신입 팀이 5일 안에 완성하기 어려움, (c) `/api/tickets/open-count` 는 변경 빈도가 명확히 낮음. 두 사람이 1번/2번 각자 정리한 PR 본문 형식으로 5분 발표 → 팀원 4명 투표 → 2번 채택.

**Action** — `CLAUDE.md` "캐시 전략" 섹션에 "변경 빈도 << 조회 빈도 인 endpoint 만 캐시" 박음. `@CacheEvict(allEntries=true)` 를 create/changeStatus 두 메서드에 적용.

**Result** — 헌법에 명문화 후 PR #5 부터는 캐시 관련 갈등 0건. Dasol 의견은 "Phase 3 에서 측정 후 재검토" 라는 후속으로 살림 → 의견 묵살 아님 + 결정 흐름 유지.

## Q4. "프로젝트의 가장 큰 약점이 무엇이고 어떻게 보완할 계획입니까?"

**Problem** — 라이프사이클 5 단계 중 "운영" 단계가 다른 4개와 깊이 차이가 보입니다. Sentry MCP 연결 + DSN 환경변수까지만 있고 실제 트래픽 + 에러 분석 사이클이 없음.

**Options** — (1) Phase 3 에서 의도적 NPE 1건 발생 → Sentry 잡힘 → MCP 위임 → root cause 검증, (2) 부하 테스트 도구로 진짜 에러 유발, (3) 그대로 두고 다른 단계로 보강.

**Decision** — 1번. 근거: (a) 5분 안에 시연 가능, (b) 면접에서 "Sentry MCP 를 쓴 사이클" 자체를 보여줄 수 있음, (c) 의도적 에러라 통제 가능.

**Action** — `MONITORING.md` 시나리오 섹션에 "의도적 NPE 발생 → Sentry 알람 → MCP 에 위임 → 후보 root cause 5개 → 실제 일치 1개" 흐름 추가. Phase 3 PR 1개로 분리.

**Result** — Phase 3 완료 시 운영 단계가 다른 4 단계와 동등한 깊이가 됨. 면접에서 "운영 단계가 약하지 않냐?" 질문 시 이 답변으로 미리 대응 가능.

## Q5. "왜 X-User-Id 헤더로 인증했나요? 본격 인증을 안 한 이유는?"

**Problem** — 5일 안에 8 PR 을 머지해야 하는데 JWT + UserDetailsService + Filter + 토큰 발급 endpoint 까지 풀로 만들면 티켓 도메인 코드를 작성할 시간이 없음.

**Options** — (1) JWT 풀 통합 (인증 + 권한 + refresh), (2) 헤더 기반 stub 인증 + Phase 3 에서 JWT 통합, (3) Spring Security 끄고 인증 자체 생략.

**Decision** — 2번. 근거: (a) 티켓 도메인이 면접에서 더 깊이 받을 질문, (b) 인증은 잘 알려진 패턴이라 Phase 3 에서 가속 가능, (c) `@PreAuthorize` 어노테이션은 그대로 두어 Phase 3 통합 시 변경 최소화.

**Action** — `XUserIdAuthFilter` 가 헤더에서 user_id 읽고 `UserRepository` 로 role 조회 후 SecurityContext 세팅. Filter 위치는 `UsernamePasswordAuthenticationFilter` 앞. `DECISIONS.md` D-007 후속에 "Phase 3 에서 JWT 통합" 명시.

**Result** — Phase 2 단계에서 `@PreAuthorize` 가 실제 강제됨 (E2E 테스트로 검증). Phase 3 통합 시 이 Filter 만 `JwtAuthenticationFilter` 로 교체하면 됨. 한계: refresh token 흐름은 Phase 3 까지 미룸.

## Q6. "AI 도구를 안 쓴다면 이 프로젝트를 5일 안에 끝낼 수 있었나요?"

**Problem** — 신입 4명 팀, 5일, 라이프사이클 5 단계 + 6 공통 필수 기능. 사람 손으로만 하면 PRD + Jira 분해만으로 Day 1 모두 소진.

**Options** — (1) AI 없이 PRD 부터 사람 손으로, (2) AI 보조 받되 검증 사이클 추가, (3) 일부만 AI (예: PRD 만), 코딩은 사람.

**Decision** — 2번. 근거: (a) Jira MCP 가 PRD 분해를 30분 만에 끝내 Day 1 의 80% 시간 회수, (b) Playwright MCP 가 시나리오 작성 시간을 1.5h → 30m 으로 줄임, (c) **AI 검증 4비법** (출처 / 재현 / 측정 / 사람 최종 판단) 을 헌법에 박아 hallucination 잡는 사이클을 같이 둠.

**Action** — Jira MCP / Playwright MCP / Claude GitHub Actions Review / Sentry MCP 4가지 통합. 각 도구 사용 시 hallucination 사례 1건 이상 잡으면 `evidence/failure-cases.md` 에 누적.

**Result** — 정량: PRD 작성 30분 (사람만 하면 4시간 추정), Playwright 시나리오 30분 (사람만 1.5시간), AI 리뷰가 잡은 진짜 이슈 3건. 한계: AI 보조 정확도 측정 자체는 Phase 3 로 미룸. **AI 도구가 5일 완주를 가능하게 했지만, 검증 사이클이 없었다면 신뢰도 낮은 결과물이 됐을 것**입니다.

## 면접 직전 체크리스트

- [ ] 위 6개 질문에 본인 경험 살을 붙여 다시 작성
- [ ] 답변마다 PR 번호 + 파일명 1~2개 인용 가능하게 외움
- [ ] "측정 못 한 부분" 솔직 인정 + Phase 3 계획 세트로 답변
- [ ] 한 답변 길이 90초~2분 (5장 × 20~25초씩)
- [ ] 수치는 정량으로, 정량 없으면 "Phase 3 에서 측정 예정" 명시
- [ ] 면접관 reaction 보고 한 장 더 펼지/마무리할지 즉석 결정
