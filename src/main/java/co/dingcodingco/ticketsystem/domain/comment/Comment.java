package co.dingcodingco.ticketsystem.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "comments",
    indexes = @Index(name = "idx_comments_ticket_id_created_at", columnList = "ticket_id, created_at")
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "ai_drafted", nullable = false)
    private boolean aiDrafted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Comment() {}

    public Comment(Long ticketId, Long authorId, String body, boolean aiDrafted) {
        this.ticketId = ticketId;
        this.authorId = authorId;
        this.body = body;
        this.aiDrafted = aiDrafted;
    }

    public Long getId() { return id; }
    public Long getTicketId() { return ticketId; }
    public Long getAuthorId() { return authorId; }
    public String getBody() { return body; }
    public boolean isAiDrafted() { return aiDrafted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
