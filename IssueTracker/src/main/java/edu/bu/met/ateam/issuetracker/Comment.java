package edu.bu.met.ateam.issuetracker;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="comment")
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer commentId;
	
	@ManyToOne
	@JoinColumn(name="issueId")
	private Issue issue;
	
	@Column(name="content")
	private String content;
	
	@ManyToOne
	@JoinColumn(name="uid")
	private User poster;
	
	@Column(name="modified_date")
	private LocalDate modifiedDate;

	public Comment() {
		
	}
	
	public Comment(Issue issue, String content, User poster, LocalDate modifiedDate) {
		this.issue = issue;
		this.content = content;
		this.poster = poster;
		this.modifiedDate = modifiedDate;
	}
	
	public void modify(String content) {
		this.content = content;
		this.modifiedDate = LocalDate.now();
	}
	
	/**
	 * @return the commentId
	 */
	public Integer getCommentId() {
		return commentId;
	}

	/**
	 * @return the associated issue
	 */
	public Issue getIssue() {
		return issue;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the poster
	 */
	public User getPoster() {
		return poster;
	}

	/**
	 * @return the postedDate
	 */
	public LocalDate getModifiedDate() {
		return modifiedDate;
	}
}
