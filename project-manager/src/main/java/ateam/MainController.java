package ateam;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.Optional;

import ateam.Task;
import ateam.TaskRepository;
import ateam.Completionlevel;
import ateam.CompletionlevelRepository;
import ateam.Project;
import ateam.ProjectRepository;
import ateam.Upw;
import ateam.UpwRepository;
import ateam.Userinfo;
import ateam.UserinfoRepository;
//import SecurityTest;

@RestController
@RequestMapping(path="/pm")
public class MainController {

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private CompletionlevelRepository completionlevelRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UpwRepository upwRepository;
	@Autowired
	private UserinfoRepository userinfoRepository;
/*	
	@GetMapping(path="/userinfo/get")
	public @ResponseBody Optional<Userinfo> getUserinfo(@RequestParam Integer uid) {
		return userinfoRepository.findById(uid);
	
	}
	
	@GetMapping(path="/upw/login")
	public @ResponseBody String login(@RequestParam String un,
			String pw) {
		Upw u = new Upw();
		u = upwRepository.findByUnAndPw(un,pw);
		SecurityTest s = new SecurityTest();
		return s.encrypt(u.getId());
	}


	@GetMapping(path="/project/add")
	public @ResponseBody String addNewProject (@RequestParam String projectname,
			String token) {
		SecurityTest s = new SecurityTest();
		int createuser=s.decrypt(token);
		if (createuser!=-1){
			if(upwRepository.existsById(createuser)){
				if(projectname.length()<40){
				Project n = new Project();
					n.setProjectname(projectname);
					n.setCreateuser(createuser);
					projectRepository.save(n);
					return s.encrypt(createuser);
				}
			}
		}
		return "-1";
	}
	@GetMapping(path="/project/delete")
	public @ResponseBody String deleteProject (@RequestParam Integer id, String token) {
		SecurityTest s = new SecurityTest();
		int createuser=s.decrypt(token);
		if (createuser!=-1){
			if(projectRepository.existsById(id)){
				projectRepository.deleteById(id);
				return s.encrypt(createuser);
				}
			return "-1";
		}
	}

	@GetMapping(path="/project/update")	
	public @ResponseBody String updateProject (@RequestParam Integer id, String projectname,
		String token) {
		SecurityTest s = new SecurityTest();
		int createuser=s.decrypt(token);
		if (createuser!=-1){
			if(upwRepository.existsById(createuser)){
				if(projectRepository.existsById(id)){
					if(projectname.length()<40){
						Project n = new Project();
						n.setId(id);
						n.setProjectname(projectname);
						n.setCreateuser(createuser);
						projectRepository.save(n);	
					}
				return s.encrypt(createuser);
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/project/all")
	public @ResponseBody Iterable<Project> getAllProjects(@RequestParam String token) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			return projectRepository.findAll();
		}
		return;
	
	}
	
	@GetMapping(path="/completionlevel/add")
	public @ResponseBody String addNewCompletionLevel (@RequestParam String levelname,
			Integer levelorder, Integer projectID, String token) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			if(projectRepository.existsById(projectID)){
				if(levelname.length()<40){
					Completionlevel n = new Completionlevel();
					n.setLevelname(levelname);
					n.setLevelorder(levelorder);
					n.setLevelcomplete(false);
					n.setProjectid(projectID);
					completionlevelRepository.save(n);
					return s.encrypt(user);
				}	
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/completionlevel/delete")
	public @ResponseBody String deleteCompletionLevel (@RequestParam Integer id, String token) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			if (completionlevelRepository.existsById(id)){
				completionlevelRepository.deleteById(id);
				return s.encrypt(user);
			}
		}	
		return "-1";
	}
	
	@GetMapping(path="/completionlevel/update")
	public @ResponseBody String updateCompletionLevel (@RequestParam Integer id, String levelname,
			Integer levelorder, Boolean levelcomplete, Integer projectID) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			if(completionlevelRepository.existsById(id)){
				if(projectRepository.existsById(projectID)){
					if(levelname.length()<40){
						if(levelorder>0){
							if(levelcomplete==true || levelcomplete==false){
								Completionlevel n = new Completionlevel();
								n.setId(id);
								n.setLevelname(levelname);
								n.setLevelorder(levelorder);
								n.setLevelcomplete(levelcomplete);
								n.setProjectid(projectID);
								completionlevelRepository.save(n);
								return s.encrypt(user);
							}
						}
					}
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/completionlevel/all")
	public @ResponseBody Iterable<Completionlevel> getAllCompletionlevels() {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			return completionlevelRepository.findAll();
		}
		return;
	}
	
	@GetMapping(path="/task/add")
	public @ResponseBody String addNewTask (@RequestParam String taskname,
			Integer taskorder, String notes, Integer userassignment, Date duedate,
			Integer level) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			Task n = new Task();
			Date date = new Date();
			if(completionlevelRepository.existsById(level)){
				if(taskname.length()<40){
					if(taskorder>0){
						if(userinfoRepository.existsById(userassignment)){
							if(duedate.getTime()>date.getTime()){
								n.setTaskname(taskname);
								n.setTaskorder(taskorder);
								n.setNotes(notes);
								n.setUserassignment(userassignment);
								n.setDuedate(duedate);
								n.setComplete(false);
								n.setLevel(level);
								taskRepository.save(n);
								return s.encrypt(user);
							}
						}
					}
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/task/delete")
	public @ResponseBody String deleteTask (@RequestParam Integer id) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			if(taskRepository.existsById(id)){
				taskRepository.deleteById(id);
				return s.encrypt(user);
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/task/update")
	public @ResponseBody String updateTask (@RequestParam Integer id, String taskname,
			Integer taskorder, String notes, Integer userassignment, Date duedate,
			Boolean complete, Integer level) {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			Task n = new Task();
			Date date = new Date();
			if(taskRepository.existsById(id)){
				if(completionlevelRepository.existsById(level)){
					if(taskname.length()<40){
						if(taskorder>0){
							if(userinfoRepository.existsById(userassignment)){
								if(duedate.getTime()>date.getTime()){
									if(complete==true||complete==false){
										n.setId(id);
										n.setTaskname(taskname);
										n.setTaskorder(taskorder);
										n.setNotes(notes);
										n.setUserassignment(userassignment);
										n.setDuedate(duedate);
										n.setComplete(complete);
										n.setLevel(level);
										taskRepository.save(n);
										return s.encrypt(user);
									}
								}
							}
						}
					}
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/task/all")
	public @ResponseBody Iterable<Task> getAllTasks() {
		SecurityTest s = new SecurityTest();
		int user = s.decrypt(token);
		if(user!=-1){
			return taskRepository.findAll();
		}
		return;
	}
*/
	@GetMapping(path="/userinfo/get")
	public @ResponseBody Optional<Userinfo> getUserinfo(@RequestParam Integer uid) {
		return userinfoRepository.findById(uid);
	
	}
	
	@GetMapping(path="/upw/login")
	public @ResponseBody String login(@RequestParam String un,
			String pw) {
		Upw u = new Upw();
		u = upwRepository.findByUnAndPw(un,pw);
		if(u != null){
			SecurityTest s = new SecurityTest();
			return s.encrypt(u.getId());
		}
		return "-1";
	}

	@GetMapping(path="/project/add")
	public @ResponseBody String addNewProject (@RequestParam String projectname,
			int createuser) {
			if(upwRepository.existsById(createuser)){
				if(projectname.length()<40){
				Project n = new Project();
					n.setProjectname(projectname);
					n.setCreateuser(createuser);
					projectRepository.save(n);
					return "1";
			}
		}
		return "-1";
	}

	@GetMapping(path="/project/delete")
	public @ResponseBody String deleteProject (@RequestParam Integer id) {
		if(projectRepository.existsById(id)){
			projectRepository.deleteById(id);
			return "1";
			}
		return "-1";
	}

	@GetMapping(path="/project/update")	
	public @ResponseBody String updateProject (@RequestParam Integer id, String projectname,
		Integer createuser) {
		if(upwRepository.existsById(createuser)){
			if(projectRepository.existsById(id)){
				if(projectname.length()<40){
					Project n = new Project();
					n.setId(id);
					n.setProjectname(projectname);
					n.setCreateuser(createuser);
					projectRepository.save(n);	
				}
			return "1";
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/project/all")
	public @ResponseBody Iterable<Project> getAllProjects() {
		return projectRepository.findAll();
	
	}
	
	@GetMapping(path="/completionlevel/add")
	public @ResponseBody String addNewCompletionLevel (@RequestParam String levelname,
			Integer levelorder, Integer projectID) {
		if(projectRepository.existsById(projectID)){
			if(levelname.length()<40){
				Completionlevel n = new Completionlevel();
				n.setLevelname(levelname);
				n.setLevelorder(levelorder);
				n.setLevelcomplete(false);
				n.setProjectid(projectID);
				completionlevelRepository.save(n);
				return "1";
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/completionlevel/delete")
	public @ResponseBody String deleteCompletionLevel (@RequestParam Integer id) {
		if (completionlevelRepository.existsById(id)){
			completionlevelRepository.deleteById(id);
			return "1";
		}
		return "-1";
	}
	
	@GetMapping(path="/completionlevel/update")
	public @ResponseBody String updateCompletionLevel (@RequestParam Integer id, String levelname,
			Integer levelorder, Boolean levelcomplete, Integer projectID) {
		if(completionlevelRepository.existsById(id)){
			if(projectRepository.existsById(projectID)){
				if(levelname.length()<40){
					if(levelorder>0){
						if(levelcomplete==true || levelcomplete==false){
							Completionlevel n = new Completionlevel();
							n.setId(id);
							n.setLevelname(levelname);
							n.setLevelorder(levelorder);
							n.setLevelcomplete(levelcomplete);
							n.setProjectid(projectID);
							completionlevelRepository.save(n);
							return "1";
						}
					}
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/completionlevel/all")
	public @ResponseBody Iterable<Completionlevel> getAllCompletionlevels() {
		return completionlevelRepository.findAll();
	
	}
	
	@GetMapping(path="/task/add")
	public @ResponseBody String addNewTask (@RequestParam String taskname,
			Integer taskorder, String notes, Integer userassignment, Date duedate,
			Integer level) {
		Task n = new Task();
		Date date = new Date();
		if(completionlevelRepository.existsById(level)){
			if(taskname.length()<40){
				if(taskorder>0){
					if(userinfoRepository.existsById(userassignment)){
						if(duedate.getTime()>date.getTime()){
							n.setTaskname(taskname);
							n.setTaskorder(taskorder);
							n.setNotes(notes);
							n.setUserassignment(userassignment);
							n.setDuedate(duedate);
							n.setComplete(false);
							n.setLevel(level);
							taskRepository.save(n);
							return "1";
						}
					}
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/task/delete")
	public @ResponseBody String deleteTask (@RequestParam Integer id) {
		if(taskRepository.existsById(id)){
			taskRepository.deleteById(id);
			return "1";
		}
		return "-1";
	}
	
	@GetMapping(path="/task/update")
	public @ResponseBody String updateTask (@RequestParam Integer id, String taskname,
			Integer taskorder, String notes, Integer userassignment, Date duedate,
			Boolean complete, Integer level) {
		Task n = new Task();
		Date date = new Date();
		if(taskRepository.existsById(id)){
			if(completionlevelRepository.existsById(level)){
				if(taskname.length()<40){
					if(taskorder>0){
						if(userinfoRepository.existsById(userassignment)){
							if(duedate.getTime()>date.getTime()){
								if(complete==true||complete==false){
									n.setId(id);
									n.setTaskname(taskname);
									n.setTaskorder(taskorder);
									n.setNotes(notes);
									n.setUserassignment(userassignment);
									n.setDuedate(duedate);
									n.setComplete(complete);
									n.setLevel(level);
									taskRepository.save(n);
									return "1";
								}
							}
						}
					}
				}
			}
		}
		return "-1";
	}
	
	@GetMapping(path="/task/all")
	public @ResponseBody Iterable<Task> getAllTasks() {
		return taskRepository.findAll();
	
	}
}