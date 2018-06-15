package edu.bu.met.ateam.issuetracker;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="uid")
	private Integer uid;
	
	@Column(name="username")
	private String username;
	
	@OneToMany(mappedBy="assignee")
	private List<Issue> assignedIssues;
	
	@OneToMany(mappedBy="owner")
	private List<Issue> ownedIssues;
	
	public User() {
		
	}
	
	public User(String username) {
		this.username = username;
		assignedIssues = new ArrayList<Issue>();
		ownedIssues = new ArrayList<Issue>();
	}
	
	public String getUsername() {
		return username;
	}
}
