package co.dingcodingco.ticketsystem.domain.ticket;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        select t from Ticket t
        where (:status is null or t.status = :status)
          and (:assigneeId is null or t.assigneeId = :assigneeId)
          and (:keyword is null or lower(t.title) like lower(concat('%', :keyword, '%')))
        order by t.priority desc, t.createdAt desc
    """)
    List<Ticket> search(@Param("status") TicketStatus status,
                        @Param("assigneeId") Long assigneeId,
                        @Param("keyword") String keyword,
                        Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.id = :id")
    Optional<Ticket> findByIdForUpdate(@Param("id") Long id);

    long countByStatus(TicketStatus status);

    List<Ticket> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);
}
