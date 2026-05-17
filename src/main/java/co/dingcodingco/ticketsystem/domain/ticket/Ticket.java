package co.dingcodingco.ticketsystem.domain.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "tickets",
    indexes = {
        @Index(name = "idx_tickets_status_created_at", columnList = "status, created_at DESC"),
        @Index(name = "idx_tickets_assignee_status",   columnList = "assignee_id, status")
    }
)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status = TicketStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "ai_summary", length = 200)
    private String aiSummary;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected Ticket() {}

    public Ticket(String title, String body, TicketPriority priority, Long requesterId) {
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.requesterId = requesterId;
    }

    public void changeStatus(TicketStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("invalid transition: " + status + " -> " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignTo(Long operatorUserId) {
        this.assigneeId = operatorUserId;
        this.updatedAt = LocalDateTime.now();
    }

    public void applyAiSummary(String summary) {
        this.aiSummary = summary;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public TicketStatus getStatus() { return status; }
    public TicketPriority getPriority() { return priority; }
    public Long getRequesterId() { return requesterId; }
    public Long getAssigneeId() { return assigneeId; }
    public String getAiSummary() { return aiSummary; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
