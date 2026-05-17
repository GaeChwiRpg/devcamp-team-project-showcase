package co.dingcodingco.ticketsystem.service;

import co.dingcodingco.ticketsystem.domain.ticket.Ticket;
import co.dingcodingco.ticketsystem.domain.ticket.TicketPriority;
import co.dingcodingco.ticketsystem.domain.ticket.TicketRepository;
import co.dingcodingco.ticketsystem.domain.ticket.TicketStatus;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 6 공통 필수 기능 매핑 (CLAUDE.md 헌법 참고):
 * - 권한 체크는 Controller/Security 레이어에서 처리, 이 서비스는 도메인 흐름만.
 * - 핵심 트랜잭션: changeStatus + assignTo (락 + 이벤트 발행 묶음).
 * - 검색/필터: search.
 * - 캐시: openTicketCount (미해결 카운터).
 * - 비동기·이벤트: TicketStatusChangedEvent 발행 (별도 핸들러에서 알림 처리).
 * - AI 보조: AiSummaryService.summarize 호출.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AiSummaryService aiSummaryService;
    private final ApplicationEventPublisher eventPublisher;

    public TicketService(TicketRepository ticketRepository,
                         AiSummaryService aiSummaryService,
                         ApplicationEventPublisher eventPublisher) {
        this.ticketRepository = ticketRepository;
        this.aiSummaryService = aiSummaryService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @CacheEvict(value = "open-ticket-count", allEntries = true)
    public Ticket create(String title, String body, TicketPriority priority, Long requesterId) {
        Ticket ticket = new Ticket(title, body, priority, requesterId);
        // AI 요약은 best-effort. 실패해도 티켓 생성은 성공.
        aiSummaryService.summarize(body).ifPresent(ticket::applyAiSummary);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> search(TicketStatus status, Long assigneeId, String keyword, int page, int size) {
        return ticketRepository.search(status, assigneeId, keyword, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException(id));
    }

    @Transactional
    @CacheEvict(value = "open-ticket-count", allEntries = true)
    public Ticket changeStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new TicketNotFoundException(id));
        TicketStatus prev = ticket.getStatus();
        ticket.changeStatus(newStatus);
        eventPublisher.publishEvent(new TicketStatusChangedEvent(ticket.getId(), prev, newStatus));
        return ticket;
    }

    @Transactional
    public Ticket assignTo(Long id, Long operatorUserId) {
        Ticket ticket = ticketRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new TicketNotFoundException(id));
        ticket.assignTo(operatorUserId);
        return ticket;
    }

    @Cacheable(value = "open-ticket-count", key = "'all'")
    @Transactional(readOnly = true)
    public long countOpen() {
        return ticketRepository.countByStatus(TicketStatus.OPEN);
    }
}
