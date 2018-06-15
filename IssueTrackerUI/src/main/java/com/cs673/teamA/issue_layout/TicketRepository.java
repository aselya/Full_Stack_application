package issue_layout;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findByProjectNameStartsWithIgnoreCase(String projectName);
	List<Ticket> findByIssueTitleStartsWithIgnoreCase(String issueTitle);
	List<Ticket> findByAuthorStartsWithIgnoreCase(String author);
	List<Ticket> findByAssigneeStartsWithIgnoreCase(String assignee);
}