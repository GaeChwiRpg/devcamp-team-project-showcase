package co.dingcodingco.ticketsystem.service;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("ticket not found: " + id);
    }
}
