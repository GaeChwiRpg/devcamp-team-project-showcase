package co.dingcodingco.ticketsystem.api.ticket;

import co.dingcodingco.ticketsystem.domain.ticket.Ticket;
import java.time.LocalDateTime;

public record TicketResponse(
    Long id,
    String title,
    String body,
    String status,
    String priority,
    Long requesterId,
    Long assigneeId,
    String aiSummary,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TicketResponse from(Ticket t) {
        return new TicketResponse(
            t.getId(),
            t.getTitle(),
            t.getBody(),
            t.getStatus().name(),
            t.getPriority().name(),
            t.getRequesterId(),
            t.getAssigneeId(),
            t.getAiSummary(),
            t.getCreatedAt(),
            t.getUpdatedAt()
        );
    }
}
