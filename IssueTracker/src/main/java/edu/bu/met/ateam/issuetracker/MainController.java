package edu.bu.met.ateam.issuetracker;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/issuetracking")
public class MainController {
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProjectRepository projRepo;
	
	@GetMapping(path="/newissue")
	public @ResponseBody String addIssue(@RequestParam Project project, @RequestParam String description, 
			@RequestParam Priority priority, @RequestParam LocalDate deadline, @RequestParam User assignee, @RequestParam User owner) {
		Issue i = new Issue(project, description, priority, deadline, assignee, owner);
		issueRepository.save(i);
		return i.toString();
	}
	
	@GetMapping(path="/testissue")
	public @ResponseBody String testIssue(@RequestParam String uname, @RequestParam String pname, @RequestParam String issue,
			@RequestParam String priority) {
		User u = new User(uname);
		Project p = new Project(pname);
		userRepo.save(u);
		projRepo.save(p);
		
		Priority pri;
		if(priority.equalsIgnoreCase("high")) {
			pri = Priority.HIGH;
		}
		else if(priority.equalsIgnoreCase("low")) {
			pri = Priority.LOW;
		}
		else {
			pri = Priority.MEDIUM;
		}
		
		Issue i = new Issue(p, issue, pri, LocalDate.now(), u, u);
		issueRepository.save(i);
		return i.toString();
	}
	
	@GetMapping(path="/issues")
	public @ResponseBody Iterable<Issue> getIssues() {
		return issueRepository.findAll();
	}
	
	@GetMapping(path="/testissues")
	public @ResponseBody String testIssues() {
		Iterable<Issue> issues = getIssues();
		StringBuilder b = new StringBuilder();
		issues.forEach(i -> b.append(i.toString() + "%n"));
		return b.toString();
	}
	
	
}
