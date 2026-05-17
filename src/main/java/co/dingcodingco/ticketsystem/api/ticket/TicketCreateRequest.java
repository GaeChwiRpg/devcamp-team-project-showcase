package co.dingcodingco.ticketsystem.api.ticket;

import co.dingcodingco.ticketsystem.domain.ticket.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketCreateRequest(
    @NotBlank @Size(max = 120) String title,
    @NotBlank String body,
    @NotNull TicketPriority priority
) {}
