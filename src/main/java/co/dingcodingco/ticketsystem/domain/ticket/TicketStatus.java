package co.dingcodingco.ticketsystem.domain.ticket;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED = Map.of(
        OPEN,        EnumSet.of(IN_PROGRESS, CLOSED),
        IN_PROGRESS, EnumSet.of(RESOLVED, OPEN),
        RESOLVED,    EnumSet.of(CLOSED, IN_PROGRESS),
        CLOSED,      EnumSet.noneOf(TicketStatus.class)
    );

    public boolean canTransitionTo(TicketStatus target) {
        return ALLOWED.getOrDefault(this, EnumSet.noneOf(TicketStatus.class)).contains(target);
    }
}
