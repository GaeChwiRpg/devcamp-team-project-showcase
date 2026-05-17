package co.dingcodingco.ticketsystem.api.ticket;

import co.dingcodingco.ticketsystem.domain.ticket.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record StatusChangeRequest(@NotNull TicketStatus status) {}
