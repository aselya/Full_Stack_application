package issue_layout;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ticket {
	@Id
	@GeneratedValue
	private Long id;

	private String projectName;

	private String issueTitle;

	private String author;

	private String assignee;

	private Boolean resolved; //Display this in icons.

	private String deadline; //Not a good idea, but easy to implement for now.

	protected Ticket() {
	}

	public Ticket(String projectName, String issueTitle, String author,
		String assignee, Boolean resolved, String deadline) {
		this.projectName = projectName;
		this.issueTitle = issueTitle;
		this.author = author;
		this.assignee = assignee;
		this.resolved = resolved;
		this.deadline = deadline;
	}

	public Long getId() {
		return id;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getIssueTitle() {
		return this.issueTitle;
	}

	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Boolean getResolved() {
		return this.resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public String getDeadline() {
		return this.deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return String.format("Issue[%d, %s, %s, %s, %s, %s, %s]", 
			id, projectName, issueTitle, author, assignee, 
			Boolean.toString(resolved), deadline);
	}

}