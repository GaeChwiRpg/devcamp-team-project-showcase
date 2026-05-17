# 발표 슬라이드 골격 — 운영 티켓 시스템 (15분 발표)

> Week 10 02-03 (책 9장: 발표 구성). 4명 팀 × 5분 데모 + 10분 Q&A 기준.
> 슬라이드 12장. 한 슬라이드 = 한 메시지 원칙.

## 발표자 분담

- 슬라이드 1~3: **Alex** (문제 → 우리 팀 소개 → 데모 1줄 요약)
- 슬라이드 4~6: **Chris** (라이프사이클 5 단계 + 핵심 기술 의사결정)
- 슬라이드 7~9: **Bora** (테스트·품질·CI 자동화)
- 슬라이드 10~12: **Dasol** (운영·AI 활용 + 회고 + Phase 3)

## 슬라이드별 메시지

### S1. 문제 정의 (30초)

- **헤드라인**: "사내 운영팀 IT 티켓이 슬랙에 흩어져 SLA 추적이 안 됨"
- **시각화**: Before(슬랙 메시지 산발) → After(상태 전이 흐름도)
- **숨은 의도**: 면접관에게 "이 팀은 비즈니스 문제부터 시작했다" 전달

### S2. 팀 소개 (30초)

- 4명 × 라이프사이클 단계 책임 매트릭스 1줄
- "5일 / 8 PR / 1 통합 demo" 한 문장

### S3. 데모 한 줄 (1분)

- 라이브 1번: 티켓 생성 → 상태 전이 → 캐시 무효화 (cURL 3개)
- 자세한 스크립트는 `DEMO-SCRIPT.md`

### S4. 라이프사이클 5 단계 (1분 30초)

- 매트릭스: 기획 (Alex/Jira MCP) · 코딩 (전원/CLAUDE.md) · 테스트 (Bora/Playwright MCP) · 리뷰 (Chris/Claude Actions) · 운영 (Dasol/Sentry MCP)
- 각 단계의 산출물 파일명 1개씩만 표시

### S5. 핵심 의사결정 1: 락 전략 (1분 30초)

- **발견**: PR #3 리뷰에서 "동시 상태 전이 중복 처리" 지적
- **선택지**: optimistic vs pessimistic
- **결정**: pessimistic (이력서·면접에서 이유 설명 가능 / `DECISIONS.md` D-006)
- **근거**: 사용자 100~1k 이하 + 충돌 비율 高 → 재시도 로직 vs 락 비교 → 락 단순화

### S6. 핵심 의사결정 2: 캐시 전략 (1분)

- 변경 빈도 << 조회 빈도 인 endpoint(`/api/tickets/open-count`)에만 적용
- 모든 변경 메서드에 `@CacheEvict(allEntries=true)` (`CLAUDE.md` 캐시 규칙)
- TTL은 Phase 3에서 검토

### S7. 테스트·CI 자동화 (1분 30초)

- Playwright MCP 시나리오 2개(성공·실패) → CI에서 매 PR마다 자동 실행
- 셀렉터 hallucination 1건 잡힌 사례 1줄 (`INTEGRATION-LOG.md` Day 3)

### S8. AI 리뷰 자동화 (1분)

- Claude GitHub Actions Review가 PR마다 한국어 리뷰 자동 작성
- 첫 시연 PR #7에서 잡은 issue 3건 (Sentry DSN 누락 / mysql 비밀번호 / Dockerfile 패턴)

### S9. AI 보조 기능 (1분)

- 요약 / 우선순위 / 답변 초안 → Phase 2는 stub, Phase 3에서 Claude API 통합
- 검증 방식: 사람 최종 판단 + hallucination 사례 `evidence/failure-cases.md` 누적

### S10. 운영 모니터링 (1분)

- Sentry SDK 통합 + Sentry MCP 가이드 (`MONITORING.md`)
- Phase 2 시연 단계에서는 가치 제한 → Phase 3에서 의도적 에러 시나리오로 보강

### S11. 회고 (1분)

- 가장 효과 컸던 도구 3개 / 가장 효과 작은 도구 1개 (`RETROSPECTIVE.md`)
- 다음 기수에 추천하는 1줄: "5일 안에 모든 단계를 완벽히 시연하지 말고, 의사결정 흐름을 우선 시연하라"

### S12. Phase 3 로드맵 + 감사 인사 (30초)

- JWT 통합 / Comment + AI 답변 초안 / 시나리오 4개 / Sentry 실제 트래픽
- 면접에서 받을 질문 6개 미리 준비 (`INTERVIEW-ANSWERS.md`)

## 발표 시간 분배

| 구간 | 시간 | 슬라이드 |
| --- | --- | --- |
| Intro | 2분 | S1~S3 |
| Lifecycle | 4분 | S4~S6 |
| Quality | 3분 | S7~S9 |
| Ops + Wrap | 3분 | S10~S12 |
| Q&A | 10분 | (별도) |

총 12분 발표 + 10분 Q&A = 22분. Live 데모 지연 대비 3분 버퍼.

## 시각 자료 가이드

- 글자 크기 28pt 이상 (뒤쪽 좌석 확인)
- 색은 3색 이내 (검정/회색/포인트 1색)
- 코드 스니펫은 7줄 이하 + 핵심만 강조
- 표는 4열 이하 (그 이상은 분리)

## Q&A 대비

면접 질문과 동일한 흐름이라 `INTERVIEW-ANSWERS.md`의 6개 답변을 그대로 활용 가능.
가장 빈번 예상 질문 Top 3:

1. "왜 pessimistic lock을 골랐는가? optimistic 안 쓴 이유는?"
2. "AI 리뷰가 잡은 진짜 이슈와 hallucination을 어떻게 구분하는가?"
3. "5일 안에 8 PR을 어떻게 분담했고, 통합 갈등은 어떻게 풀었는가?"
