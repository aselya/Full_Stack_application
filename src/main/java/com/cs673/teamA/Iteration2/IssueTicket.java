package com.cs673.teamA.Iteration2;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IssueTicket {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long issueId;
	private Long projectId;
	private String name;
	private String description;
	private Integer priority;
	private Date dateCreated;
	private Date deadline;
	private Long assigneeId;
	private Long ownerId;
	private boolean resolved;
	
	public IssueTicket() {
	
	}

	/**
	 * @param issueId the issueId to set
	 */
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	/**
	 * @return the issueId
	 */
	public Long getIssueId() {
		return issueId;
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * @return the assigneeId
	 */
	public Long getAssigneeId() {
		return assigneeId;
	}

	/**
	 * @param assigneeId the assigneeId to set
	 */
	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	/**
	 * @return the ownerId
	 */
	public Long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the resolved
	 */
	public boolean isResolved() {
		return resolved;
	}

	/**
	 * @param resolved the resolved to set
	 */
	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
}
