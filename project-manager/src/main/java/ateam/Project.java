package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Project{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer ID;
	private String projectname;
	public Integer getId(){
		return ID;
	}
	public void setId(Integer ID){
		this.ID=ID;
	}
	public String getProjectname(){
		return projectname;
	}
	public void setProjectname(String projectname){
		this.projectname=projectname;
	}
}