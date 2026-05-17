# 라이브 데모 스크립트 — 5분

> 운영자(operator) 시점에서 **티켓 생성 → 트리아지 → 상태 전이 → 캐시 무효화** 흐름을 5분 안에 보여준다.
> 발표자: **Alex**(터미널 1·cURL) + **Bora**(터미널 2·DB 콘솔) 동시 진행.

## 사전 준비 (발표 시작 30분 전)

```bash
# 시연 머신에서 한 번만
docker compose up -d            # mysql + redis + app
curl -fsS http://localhost:8080/actuator/health | jq .status
# → "UP" 확인. 아니면 Plan B (H2 inmemory) 로 fallback
```

대안: `./gradlew bootRun` (H2 인메모리, mysql/redis 컨테이너 실패 시).

## 시나리오 (5분)

### 0분 — 도메인 한 줄 소개 (15초)

> "사내 운영팀 IT 티켓을 받고 → AI가 1줄 요약 → 운영자가 트리아지 → 상태 전이 → 종료. 그 전체 5단계를 60초 안에 보여드리겠습니다."

### 0:15 — 티켓 생성 (member 페르소나, 1분)

```bash
curl -X POST http://localhost:8080/api/tickets \
  -H 'Content-Type: application/json' \
  -H 'X-User-Id: 3' \
  -d '{
    "title": "사내 메일 서버 응답이 느려요",
    "body": "오늘 오전 10시부터 외부 메일 발송이 30초씩 지연됩니다.",
    "priority": "HIGH"
  }' | jq .
```

**보여줄 포인트**:

- 응답 `id`, `status: "OPEN"`, `aiSummary` (Phase 2 stub: "[STUB] 사내 메일 서버 응답이 느려요")
- 한 마디: "Phase 3 에서 Claude API 로 교체. 지금은 stub."

### 1:15 — 운영자 목록 조회 (operator 페르소나, 30초)

```bash
curl 'http://localhost:8080/api/tickets?status=OPEN' \
  -H 'X-User-Id: 2' | jq '.[].title'
```

**보여줄 포인트**: priority desc + created_at desc 정렬 (높은 우선순위가 위로)

### 1:45 — 상태 전이: OPEN → IN_PROGRESS (1분)

```bash
TICKET_ID=$(curl -s 'http://localhost:8080/api/tickets?status=OPEN' \
  -H 'X-User-Id: 2' | jq -r '.[0].id')

curl -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/status" \
  -H 'Content-Type: application/json' \
  -H 'X-User-Id: 2' \
  -d '{"status":"IN_PROGRESS"}' | jq .
```

**보여줄 포인트**:

- `TicketStatus.canTransitionTo` 로 잘못된 전이는 400 (예: CLOSED → IN_PROGRESS)
- pessimistic 락 동작 → DB 콘솔(터미널 2)에서 `SHOW PROCESSLIST` 잠깐 띄우면서 한 마디

### 2:45 — 캐시 무효화 시연 (1분)

```bash
# 1) 변경 전: open count 캐시 채우기
curl http://localhost:8080/api/tickets/open-count | jq .   # ex: 3

# 2) 새 티켓 생성 (캐시 무효화 트리거)
curl -X POST http://localhost:8080/api/tickets ... # (위와 동일)

# 3) 변경 후: 카운트 재계산
curl http://localhost:8080/api/tickets/open-count | jq .   # ex: 4
```

**보여줄 포인트**: `@CacheEvict(allEntries=true)` 가 create/changeStatus 두 메서드에서 동작 (`CLAUDE.md` 캐시 규칙)

### 3:45 — AI 답변 초안 (Comment) 시연 (45초, Phase 3 mock)

```bash
# Phase 2 단계: 답변 초안 stub
curl -X POST "http://localhost:8080/api/tickets/$TICKET_ID/comments" \
  -H 'Content-Type: application/json' \
  -H 'X-User-Id: 2' \
  -d '{"body":"확인 중입니다.", "aiDrafted": true}' | jq .
```

**보여줄 포인트**: `aiDrafted` 플래그로 사람 검토 여부 추적. AI hallucination 시 `evidence/failure-cases.md` 누적.

### 4:30 — 30초 마무리

> "5일 안에 라이프사이클 5 단계 + 6 공통 필수 기능을 8 PR 로 시연했습니다.
> Phase 3 에서 JWT, Sentry 실 트래픽, 시나리오 4개 확장 예정입니다."

## 발표 중 사고 대비

### 케이스 A: docker compose 가 죽었다

`./gradlew bootRun --args='--spring.profiles.active=local'` 로 H2 인메모리 fallback.
→ `data.sql` 의 시드 사용자 그대로 사용 가능.

### 케이스 B: 8080 포트 충돌

`SERVER_PORT=8090 ./gradlew bootRun` 로 다시 시작 + cURL 의 8080을 8090으로 일괄 치환 (시연 스크립트 변수화 필요).

### 케이스 C: AI Review 결과 못 보여주기

`gh pr view 7 --comments` 로 머지된 PR의 AI 리뷰 코멘트 캡처해 둔 화면을 띄운다.
사전에 PR #7 의 AI 리뷰 본문을 슬라이드 S8 에 캡처 이미지로 박아둠.

## 면접관용 코멘트 (데모 후 받을 질문 대비)

- "왜 X-User-Id 헤더로 인증?" → "Phase 2 한정 시연용. Phase 3 JWT 통합 예정. `DECISIONS.md` D-007 후속에 적힘."
- "AI 요약 정확도?" → "Phase 2 단계 stub 이라 측정 X. Phase 3 에서 사람 라벨링 50건 vs AI 결과 일치율 측정 예정."
- "동시 변경 충돌 빈도?" → "현재 `@Lock(PESSIMISTIC_WRITE)` 로 충돌 자체를 방지. 측정은 Phase 3 부하 테스트에서 hit rate 확인."

## Plan B: 데모 못 하는 경우 (영상 1분 30초)

- 사전 녹화 영상 1분 30초 (위 시나리오를 30초씩 압축)
- 슬라이드 S3 자리에 배치
- 스피커가 음성 해설만 진행
