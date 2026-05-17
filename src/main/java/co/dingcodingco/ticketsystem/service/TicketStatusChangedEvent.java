package co.dingcodingco.ticketsystem.service;

import co.dingcodingco.ticketsystem.domain.ticket.TicketStatus;

/**
 * 비동기/이벤트 흐름의 출발점.
 * 별도 핸들러(예: NotificationListener)에서 신청자 알림 발송.
 */
public record TicketStatusChangedEvent(
    Long ticketId,
    TicketStatus previous,
    TicketStatus next
) {}
