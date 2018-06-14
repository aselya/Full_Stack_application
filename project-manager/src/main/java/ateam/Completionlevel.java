package ateam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Completionlevel{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer ID;
	private String levelname;
	private Integer levelorder;
	private Boolean levelcomplete;
	private Integer projectID;
	public Integer getId(){
		return ID;
	}
	public void setId(Integer ID){
		this.ID=ID;
	}
	public String getLevelname(){
		return levelname;
	}
	public void setLevelname(String levelname){
		this.levelname=levelname;
	}
	public Integer getLevelorder(){
		return levelorder;
	}
	public void setLevelorder(Integer levelorder){
		this.levelorder=levelorder;
	}
	public Boolean getLevelcomplete(){
		return levelcomplete;
	}
	public void setLevelcomplete(Boolean levelcomplete){
		this.levelcomplete=levelcomplete;
	}
	public Integer getProjectid(){
		return projectID;
	}
	public void setProjectid(Integer Projectid){
		this.projectID=projectID;
	}
}