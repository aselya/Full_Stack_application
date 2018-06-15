package edu.bu.met.ateam.issuetracker;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="project")
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pid")
	private Integer pid;
	
	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy="project")
	@Column(name="issues")
	private List<Issue> issues;
	
	public Project() {
		
	}
	
	public Project(String name) {
		this.name = name;
		this.issues = new ArrayList<Issue>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addIssue(Issue issue) {
		issues.add(issue);
	}
	
	public void removeIssue(Issue issue) {
		issues.remove(issue);
	}
	
	public List<Issue> getIssues() {
		return issues;
	}
}
