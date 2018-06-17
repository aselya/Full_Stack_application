package ateam;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

import ateam.Task;
import ateam.TaskRepository;
import ateam.Completionlevel;
import ateam.CompletionlevelRepository;
import ateam.Project;
import ateam.ProjectRepository;

@RestController
@RequestMapping(path="/pm")
public class MainController {

	@Autowired
	private ProjectRepository projectRepository;
	private CompletionlevelRepository completionlevelRepository;
	private TaskRepository taskRepository;

	@GetMapping(path="/project/add")
	public @ResponseBody String addNewProject (@RequestParam String projectname,
			Integer createuser) {
		Project n = new Project();
		n.setProjectname(projectname);
		n.setCreateuser(createuser);
		projectRepository.save(n);
		return "Saved";
	}
	@GetMapping(path="/project/delete")
	public @ResponseBody String deleteProject (@RequestParam Long id) {
		projectRepository.deleteById(id);
		return "Deleted";
	}
	@GetMapping(path="/project/update")
	public @ResponseBody String updateProject (@RequestParam Long id, String projectname,
		Integer createuser) {
		Project n = new Project();
		n.setId(id);
		n.setProjectname(projectname);
		n.setCreateuser(createuser);
		projectRepository.save(n);	
		return "Updated";
	}
	@GetMapping(path="/project/all")
	public @ResponseBody Iterable<Project> getAllProjects() {
		return projectRepository.findAll();
	
	}
	
	@GetMapping(path="/completionlevel/add")
	public @ResponseBody String addNewCompletionLevel (@RequestParam String levelname,
			Integer levelorder, Long projectID) {
		Completionlevel n = new Completionlevel();
		n.setLevelname(levelname);
		n.setLevelorder(levelorder);
		n.setLevelcomplete(false);
		n.setProjectid(projectID);
		completionlevelRepository.save(n);
		return "Saved";
	}
	@GetMapping(path="/completionlevel/delete")
	public @ResponseBody String deleteCompletionLevel (@RequestParam Long id) {
		completionlevelRepository.deleteById(id);
		return "Deleted";
	}
	@GetMapping(path="/completionlevel/update")
	public @ResponseBody String updateCompletionLevel (@RequestParam Long id, String levelname,
			Integer levelorder, Boolean levelcomplete, Long projectID) {
		Completionlevel n = new Completionlevel();
		n.setId(id);
		n.setLevelname(levelname);
		n.setLevelorder(levelorder);
		n.setLevelcomplete(levelcomplete);
		n.setProjectid(projectID);
		completionlevelRepository.save(n);
		return "Updated";
	}
	@GetMapping(path="/completionlevel/all")
	public @ResponseBody Iterable<Completionlevel> getAllCompletionlevels() {
		return completionlevelRepository.findAll();
	
	}
	
		@GetMapping(path="/task/add")
	public @ResponseBody String addNewTask (@RequestParam String taskname,
			Integer taskorder, String notes, Long userassignment, Date duedate,
			Long level) {
		Task n = new Task();
		n.setTaskname(taskname);
		n.setTaskorder(taskorder);
		n.setNotes(notes);
		n.setUserassignment(userassignment);
		n.setDuedate(duedate);
		n.setComplete(false);
		n.setLevel(level);
		taskRepository.save(n);
		return "Saved";
	}
	@GetMapping(path="/task/delete")
	public @ResponseBody String deleteTask (@RequestParam Long id) {
		taskRepository.deleteById(id);
		return "Deleted";
	}
	@GetMapping(path="/task/update")
	public @ResponseBody String updateTask (@RequestParam Long id, String taskname,
			Integer taskorder, String notes, Long userassignment, Date duedate,
			Boolean complete, Long level) {
		Task n = new Task();
		n.setId(id);
		n.setTaskname(taskname);
		n.setTaskorder(taskorder);
		n.setNotes(notes);
		n.setUserassignment(userassignment);
		n.setDuedate(duedate);
		n.setComplete(complete);
		n.setLevel(level);
		taskRepository.save(n);
		return "Updated";
	}
	@GetMapping(path="/task/all")
	public @ResponseBody Iterable<Task> getAllTasks() {
		return taskRepository.findAll();
	
	}
}