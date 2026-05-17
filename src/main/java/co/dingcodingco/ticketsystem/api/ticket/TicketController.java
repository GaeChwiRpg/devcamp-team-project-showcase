package co.dingcodingco.ticketsystem.api.ticket;

import co.dingcodingco.ticketsystem.domain.ticket.TicketStatus;
import co.dingcodingco.ticketsystem.service.TicketService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Phase 2 한정 인증 처리: X-User-Id 헤더로 requester 식별 (시연용).
 * Phase 3에서 JWT + UserDetailsService + @AuthenticationPrincipal 통합 (DECISIONS.md D-007 후속).
 *
 * 권한 정책 (Phase 3에서 실제 강제, Phase 2는 어노테이션만 명시):
 * - 생성: 인증된 사용자
 * - 목록·상세: operator 이상
 * - 상태 전이: operator 이상
 * - 담당자 배정: admin
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@Valid @RequestBody TicketCreateRequest request,
                                 @RequestHeader(name = "X-User-Id") Long requesterId) {
        return TicketResponse.from(
            ticketService.create(request.title(), request.body(), request.priority(), requesterId)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public List<TicketResponse> list(@RequestParam(required = false) TicketStatus status,
                                     @RequestParam(required = false) Long assigneeId,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        return ticketService.search(status, assigneeId, keyword, page, size).stream()
            .map(TicketResponse::from)
            .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public TicketResponse get(@PathVariable Long id) {
        return TicketResponse.from(ticketService.findById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public TicketResponse changeStatus(@PathVariable Long id,
                                       @Valid @RequestBody StatusChangeRequest request) {
        return TicketResponse.from(ticketService.changeStatus(id, request.status()));
    }

    @PutMapping("/{id}/assignee")
    @PreAuthorize("hasRole('ADMIN')")
    public TicketResponse changeAssignee(@PathVariable Long id,
                                         @Valid @RequestBody AssigneeChangeRequest request) {
        return TicketResponse.from(ticketService.assignTo(id, request.assigneeId()));
    }
}
