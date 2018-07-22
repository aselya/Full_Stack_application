//Comment
package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//Comment
import javax.persistence.Id;

@Entity
public class Requirement{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String requirementname;
	private Integer requirementorder;
	private String description;
	private Integer projectid;
	
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public String getRequirementname(){
		return requirementname;
	}
	public void setRequirementname(String requirementname){
		this.requirementname=requirementname;
	}
	public Integer getRequirementorder(){
		return requirementorder;
	}
	public void setRequirementorder(Integer requirementorder){
		this.requirementorder=requirementorder;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public Integer getProjectid(){
		return projectid;
	}
	public void setProjectid(Integer projectid){
		this.projectid=projectid;
	}
}