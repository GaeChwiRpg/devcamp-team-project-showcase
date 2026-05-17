# Monitoring — Sentry MCP + Spring Actuator

> 운영 단계 산출물. Phase 2에서 sample 설정 + Phase 3에서 시연용 에러 분석 흐름 실증.

## 도구 선택

- **Sentry MCP** (책 10주차 06)
  - Claude Code에 연결 후 에러 분석 위임
  - 시연용 단일 인스턴스 환경에 적합
- (선택) Langfuse — Phase 4에서 LLM observability 검토

## 설정

### 1. Sentry SDK 통합 (Phase 3 작업)

```gradle
implementation 'io.sentry:sentry-spring-boot-starter-jakarta:7.18.0'
```

`application.yml`:

```yaml
sentry:
  dsn: ${SENTRY_DSN:}
  traces-sample-rate: 0.2
  send-default-pii: false  # 운영 데이터에 PII 포함될 위험 차단
```

### 2. Claude Code Sentry MCP 연결 (운영자 로컬)

```bash
claude mcp add sentry-mcp \
  -e SENTRY_AUTH_TOKEN=$SENTRY_AUTH_TOKEN \
  -- npx -y @sentry/mcp-server@latest
```

### 3. Spring Actuator 기본

`application.yml`에 노출 endpoint 명시 (이미 baseline에 포함):
- `/actuator/health`
- `/actuator/metrics`
- `/actuator/caches`
- `/actuator/prometheus`

## 운영 시나리오

### 시연용 에러 분석 (Phase 3에서 실증)

1. 의도적으로 에러 발생 (예: 잘못된 status 전이)
2. Sentry에 이벤트 적재 확인
3. Claude Code에 prompt:

   ```
   [페] 너는 본 시스템의 SRE.
   [목] sentry-mcp로 최근 1시간 issue를 조회해서 root cause 후보 3개와 우선순위.
   [형] 표 형식 (issue_id / title / count / suspect / 우선순위)
   [제] 추측 수치 금지. 실제 stacktrace 인용.
   ```

4. AI가 후보 제시 → 운영자가 검증 + 후속 fix PR

### 평가 기준

- root cause 후보 3개 중 1개 이상이 실제 fix와 일치 (정확도)
- AI hallucination 없음 (없는 stacktrace 만들어내지 않음)

## 부하 테스트 (Week 10 보강)

- 도구: hey 또는 wrk + Sentry traces
- 시나리오: 50 동시 요청 × 5분
- 측정: p95 응답 시간 + Sentry traces sample 비율 + Sentry에서 경고 발생 여부

## 한계와 다음 단계

- 시연 환경에 실제 트래픽이 없어 운영 학습 깊이 제한
- Phase 4에서 부하 테스트 + 실제 에러 시나리오 5개 이상 시연
- Langfuse는 AI 응답 품질이 production에 도달했을 때 도입
