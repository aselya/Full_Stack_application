package edu.bu.met.ateam.issuetracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="issue")
public class Issue {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer issueId;
	
	@ManyToOne
	@JoinColumn(name="pid")
	private Project project;
	
	@Column(name="description")
	private String description;
	
	@Column(name="priority")
	private Priority priority;
	
	@Column(name="deadline")
	private LocalDate deadline;
	
	@ManyToOne
	@JoinColumn(name="assignee")
	private User assignee;
	
	@ManyToOne
	@JoinColumn(name="owner")
	private User owner;
	
	@OneToMany(mappedBy="issue")
	@Column(name="comments")
	private List<Comment> comments;
	
	@Column(name="resolved")
	private boolean resolved;
	
	public Issue() {
		
	}
	
	public Issue(Project project, String description, Priority priority, LocalDate deadline, User assignee, User owner) {
		this.project = project;
		this.description = description;
		this.priority = priority;
		this.deadline = deadline;
		this.assignee = assignee;
		this.owner = owner;
		this.comments = new ArrayList<Comment>();
		this.resolved = false;
	}

	/**
	 * @return the issueId
	 */
	public Integer getId() {
		return issueId;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * @return the deadline
	 */
	public LocalDate getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	/**
	 * @return the assignee
	 */
	public User getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comment Add a comment to the issue
	 */
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	/**
	 * @param comment Remove a comment from the issue
	 */
	public void removeComment(Comment comment) {
		comments.remove(comment);
	}
	
	/**
	 * @return whether the issue has been resolved or not
	 */
	public boolean isResolved() {
		return resolved;
	}
	
	/**
	 * @param resolution The current state of the issue's resolution
	 */
	public void setResolution(boolean resolution) {
		this.resolved = resolution;
	}
	
	public String toString() {
		return String.format("Id: %d, Project: %s, Issue: %s, Priority: %s, Deadline: %s, Assignee: %s, Owner: %s, Resolved: %b",
				issueId, project.getName(), description, priority, deadline, assignee.getUsername(), owner.getUsername(), resolved);
	}
}
