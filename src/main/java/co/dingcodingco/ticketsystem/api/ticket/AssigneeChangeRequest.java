package co.dingcodingco.ticketsystem.api.ticket;

import jakarta.validation.constraints.NotNull;

public record AssigneeChangeRequest(@NotNull Long assigneeId) {}
