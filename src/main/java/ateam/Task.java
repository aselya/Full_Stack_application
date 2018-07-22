//Comment
package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//Comment
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Task{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String taskname;
	private Integer taskorder;
	private String notes;
	private Integer userassignment;
	private Date duedate;
	private Boolean complete;
	private Integer level;
	
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public String getTaskname(){
		return taskname;
	}
	public void setTaskname(String taskname){
		this.taskname=taskname;
	}
	public Integer getTaskorder(){
		return taskorder;
	}
	public void setTaskorder(Integer taskorder){
		this.taskorder=taskorder;
	}
	public String getNotes(){
		return notes;
	}
	public void setNotes(String notes){
		this.notes=notes;
	}
	public Integer getUserassignment(){
		return userassignment;
	}
	public void setUserassignment(Integer userassignment){
		this.userassignment=userassignment;
	}
	public Date getDuedate(){
		return duedate;
	}
	public void setDuedate(Date duedate){
		this.duedate=duedate;
	}
	public Boolean getComplete(){
		return complete;
	}
	public void setComplete(Boolean complete){
		this.complete=complete;
	}
	public Integer getLevel(){
		return level;
	}
	public void setLevel(Integer level){
		this.level=level;
	}
}