package co.dingcodingco.ticketsystem.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AI 보조 기능: 티켓 본문 → 1줄 요약.
 *
 * 시연 단계에서는 stub. Phase 3에서 Claude API 통합 예정.
 * Phase 3 통합 후 다음을 evidence로 남긴다:
 * - 운영자 채택률 (AI 요약 그대로 사용 비율)
 * - hallucination 사례
 * - PII 마스킹 옵션
 */
@Service
public class AiSummaryService {

    private final boolean enabled;

    public AiSummaryService(@Value("${app.ai.enabled:false}") boolean enabled) {
        this.enabled = enabled;
    }

    public Optional<String> summarize(String body) {
        if (!enabled || body == null || body.isBlank()) {
            return Optional.empty();
        }
        // TODO Phase 3: Claude API 호출. 현재는 단순 잘라내기 stub.
        String stub = body.length() > 60 ? body.substring(0, 57) + "..." : body;
        return Optional.of(stub);
    }
}
