package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Project{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String projectname;
	private Integer createuser;
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id=id;
	}
	public String getProjectname(){
		return projectname;
	}
	public void setProjectname(String projectname){
		this.projectname=projectname;
	}
	public Integer getCreateuser(){
		return createuser;
	}
	public void setCreateuser(Integer createuser){
		this.createuser=createuser;
	}
}