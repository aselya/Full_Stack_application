package com.cs673.teamA.Iteration2;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long commentId;
	private Long issueId;
	private String content;
	private Long posterId;
	private Date dateModified;
	
	public Comment() {
		
	}
	
	/**
	 * @return the commentId
	 */
	public Long getCommentId() {
		return commentId;
	}

	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	/**
	 * @return the issueId
	 */
	public Long getIssueId() {
		return issueId;
	}

	/**
	 * @param issueId the issueId to set
	 */
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the posterId
	 */
	public Long getPosterId() {
		return posterId;
	}

	/**
	 * @param posterId the posterId to set
	 */
	public void setPosterId(Long posterId) {
		this.posterId = posterId;
	}

	/**
	 * @return the dateModified
	 */
	public Date getDateModified() {
		return dateModified;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	
}
