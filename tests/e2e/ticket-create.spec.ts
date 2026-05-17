import { test, expect } from '@playwright/test';

/**
 * Phase 2 sample 시나리오. Phase 3에서 본격 확장 (login-flow, ticket-triage,
 * ticket-status-transition 등 추가).
 *
 * 시나리오: 신청자가 티켓 생성 → 응답이 OPEN + AI 요약(현재 stub)이 포함되는지 검증.
 */
test('티켓 생성: 응답이 OPEN 상태로 생성된다', async ({ request }) => {
  const response = await request.post('/api/tickets', {
    data: {
      title: 'E2E 테스트 티켓',
      body: '본문 — Playwright MCP 자동 시나리오에서 생성됨.',
      priority: 'MEDIUM',
    },
  });

  expect(response.status()).toBe(201);

  const body = await response.json();
  expect(body.title).toBe('E2E 테스트 티켓');
  expect(body.status).toBe('OPEN');
  expect(body.priority).toBe('MEDIUM');
  expect(body.requesterId).toBe(3);
});

test('티켓 생성 실패: title 빈 값은 400', async ({ request }) => {
  const response = await request.post('/api/tickets', {
    data: { title: '', body: '본문', priority: 'LOW' },
  });

  expect(response.status()).toBe(400);
});
